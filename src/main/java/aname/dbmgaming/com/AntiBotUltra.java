package aname.dbmgaming.com;

import aname.dbmgaming.com.commands.AntiBotUltraCommands;
import aname.dbmgaming.com.config.CustomConfig;
import aname.dbmgaming.com.datasource.DataSource;
import aname.dbmgaming.com.datasource.MySQLDataSource;
import aname.dbmgaming.com.datasource.YamlDataSource;
import aname.dbmgaming.com.protection.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class AntiBotUltra extends JavaPlugin {

    private AlternativeProxyProtection alternativeProxyProtection;
    private DnsblProtection dnsblProtection;
    private BotScoutProtection botScoutProtection;
    private StopForumSpamProtection stopForumSpamProtection;
    private BotProtection botProtection;
    private CustomConfig customConfig;
    private DataSource dataSource;

    @Override
    public void onEnable() {
        customConfig = new CustomConfig(this);
        customConfig.saveDefaultConfig();
        botProtection = new BotProtection(this);
        alternativeProxyProtection = new AlternativeProxyProtection(this);
        stopForumSpamProtection = new StopForumSpamProtection();
        botScoutProtection = new BotScoutProtection();
        dnsblProtection = new DnsblProtection();

        getConfig().getStringList("protections.alternative.with-href").forEach(url -> alternativeProxyProtection.downloadWithHref(url));
        getConfig().getStringList("protections.alternative.without-href").forEach(url -> alternativeProxyProtection.downloadWithoutHref(url));

        getConfig().getStringList("protections.stopforumspam.asn.blacklist").forEach(asn -> stopForumSpamProtection.addToAsnBlacklist(asn));
        getConfig().getStringList("protections.stopforumspam.country.blacklist").forEach(country -> stopForumSpamProtection.addToCountryBlacklist(country));

        getConfig().getStringList("protections.dnsbl.blacklist").forEach(dnsbl -> dnsblProtection.addLookupService(dnsbl));
        dnsblProtection.setRetries(getConfig().getInt("protections.dnsbl.retries"));
        dnsblProtection.setTimeout(getConfig().getInt("protections.dnsbl.timeout"));

        switch (getConfig().getString("mysql.type").toLowerCase()) {
            case "yaml":
                dataSource = new YamlDataSource();
                break;
            case "mysql":
                try {
                    dataSource = new MySQLDataSource(getConfig().getString("mysql.host"), getConfig().getString("mysql.port")
                            , getConfig().getString("mysql.username"), getConfig().getString("mysql.password"), getConfig().getString("mysql.arguments"));
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                dataSource = new YamlDataSource();
                getConfig().set("mysql.type", "yaml");
                getCustomConfig().saveConfig();
                break;
        }
        alternativeProxyProtection.getAlternativeProxies().forEach(s -> dataSource.blacklistPlayerIp(s));
        getConfig().getStringList("protections.dnsbl.blacklist").forEach(s -> dnsblProtection.addLookupService(s));
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
        Bukkit.getPluginCommand("antibot-ultra").setExecutor(new AntiBotUltraCommands(this));
    }

    @Override
    public void onDisable() {
    }

    @Override
    public FileConfiguration getConfig() {
        return customConfig.getCustomConfig();
    }

    /*Strange... right?*/
    public CustomConfig getCustomConfig() {
        return customConfig;
    }

    public StopForumSpamProtection getStopForumSpamProtection() {
        return stopForumSpamProtection;
    }

    public BotProtection getBotProtection() {
        return botProtection;
    }

    public BotScoutProtection getBotScoutProtection() {
        return botScoutProtection;
    }

    public AlternativeProxyProtection getAlternativeProxyProtection() {
        return alternativeProxyProtection;
    }

    public DnsblProtection getDnsblProtection() {
        return dnsblProtection;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
