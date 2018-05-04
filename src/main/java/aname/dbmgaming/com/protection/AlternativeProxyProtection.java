package aname.dbmgaming.com.protection;

import aname.dbmgaming.com.AntiBotUltra;
import aname.dbmgaming.com.util.WebsiteUtils;
import org.bukkit.Bukkit;

import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlternativeProxyProtection {

    private final String IP_REGEX_PATTERN = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
    private HashSet<String> alternativeProxies = new HashSet<>();
    private AntiBotUltra antiBotUltra;

    public AlternativeProxyProtection(AntiBotUltra antiBotUltra) {
        this.antiBotUltra = antiBotUltra;
    }

    public boolean isAlternativeProxiesBlacklisted(String ip) {
        return alternativeProxies.contains(ip);
    }

    public void downloadWithHref(String url) {
        if (url.isEmpty()) {
            Bukkit.getLogger().log(Level.INFO, "[AntiBot-Ultra] URL is empty...");
            return;
        }
        Pattern IP_PATTERN = Pattern.compile(IP_REGEX_PATTERN);
        Bukkit.getScheduler().runTaskAsynchronously(antiBotUltra, () -> {
            List<String> list = WebsiteUtils.getHRefs(url);
            list.stream().filter(urlPages -> urlPages.contains("/20")).map(WebsiteUtils::getTextPreserveLines).forEach(textOnPages -> textOnPages.forEach(eachLine -> {
                Matcher matcher = IP_PATTERN.matcher(eachLine);
                boolean lineContainsIP = matcher.find();
                if (lineContainsIP) {
                    alternativeProxies.add(eachLine.split("</p")[0].split(":")[0].replace("[^\\d.]", ""));
                }
            }));
        });
    }

    public void downloadWithoutHref(String url) {
        if (url.isEmpty()) {
            Bukkit.getLogger().log(Level.INFO, "[AntiBot-Ultra] URL is empty...");
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(antiBotUltra, () -> alternativeProxies.add(WebsiteUtils.getText(url)));
    }

    public HashSet<String> getAlternativeProxies() {
        return alternativeProxies;
    }
}
