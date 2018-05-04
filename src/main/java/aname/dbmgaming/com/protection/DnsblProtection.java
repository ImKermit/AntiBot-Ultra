package aname.dbmgaming.com.protection;

import sun.net.dns.ResolverConfiguration;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class DnsblProtection {

    private static String[] RECORD_TYPES = {"A"};
    private DirContext ictx;
    private ArrayList<String> lookupServices = new ArrayList<>();

    private int timeout = 4000;
    private int retries = 1;

    public DnsblProtection() {
        StringBuilder dnsServers = new StringBuilder("");
        List<String> nameservers = ResolverConfiguration.open().nameservers();
        for (Object dns : nameservers) {
            dnsServers.append("dns://").append(dns).append(" ");
        }

        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
        env.put("com.sun.jndi.dns.timeout.initial", String.valueOf(timeout));
        env.put("com.sun.jndi.dns.timeout.retries", String.valueOf(retries));
        env.put(Context.PROVIDER_URL, dnsServers.toString());

        try {
            ictx = new InitialDirContext(env);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public void addLookupService(String service) {
        lookupServices.add(service);
    }

    public boolean isBlocked(String ip) {
        String[] parts = ip.split("\\.");

        StringBuilder buffer = new StringBuilder();

        Arrays.stream(parts).forEach(part -> {
            buffer.insert(0, ".");
            buffer.insert(0, part);
        });
        ip = buffer.toString();

        for (String service : lookupServices) {
            String lookup = ip + service;
            try {
                Attributes attributes = ictx.getAttributes(lookup, RECORD_TYPES);
                Attribute attribute = attributes.get("A");
                if (attribute != null) {
                    return true;
                }
            } catch (NamingException ignored) {
                return false;
            }
        }
        return false;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }
}
