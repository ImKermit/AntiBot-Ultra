package aname.dbmgaming.com;

import aname.dbmgaming.com.datasource.DataSource;
import aname.dbmgaming.com.protection.*;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerListener implements Listener {

    private JsonObject stopForumSpamObject;

    private AlternativeProxyProtection alternativeProxyProtection;
    private BotProtection botProtection;
    private BotScoutProtection botScoutProtection;
    private DnsblProtection dnsblProtection;
    private StopForumSpamProtection stopForumSpamProtection;
    private DataSource dataSource;

    private String alternativeProxyKickMsg;
    private String botProtectionKickMsg;
    private String botProtectionWhitelistMsg;
    private String botScoutProtectionKickMsg;
    private String dnsblProtectionKickMsg;
    private String stopForumSpamAsnKickMsg;
    private String stopForumSpamCountryKickMsg;
    private String stopForumSpamProxyKickMsg;
    private String localBlacklistKickMsg;

    private int requiredPlayTime;

    private boolean alternativeProxyEnabled;
    private boolean botProtectionEnabled;
    private boolean botScoutProtectionEnabled;
    private boolean dnsblProtectionEnabled;
    private boolean stopForumSpamAsnEnabled;
    private boolean stopForumSpamCountryEnabled;
    private boolean stopForumSpamProxyEnabled;
    private boolean localBlacklistEnabled;

    public PlayerListener(AntiBotUltra antiBotUltra) {
        this.alternativeProxyProtection = antiBotUltra.getAlternativeProxyProtection();
        this.botProtection = antiBotUltra.getBotProtection();
        this.botScoutProtection = antiBotUltra.getBotScoutProtection();
        this.dnsblProtection = antiBotUltra.getDnsblProtection();
        this.stopForumSpamProtection = antiBotUltra.getStopForumSpamProtection();
        this.dataSource = antiBotUltra.getDataSource();

        this.alternativeProxyKickMsg = antiBotUltra.getConfig().getString("messages.alternative-kick");
        System.out.println(alternativeProxyKickMsg);
        this.botProtectionKickMsg = antiBotUltra.getConfig().getString("messages.bot-protection.kick");
        System.out.println(botProtectionKickMsg);
        this.botProtectionWhitelistMsg = antiBotUltra.getConfig().getString("messages.bot-protection.whitelist");
        System.out.println(botProtectionWhitelistMsg);
        this.botScoutProtectionKickMsg = antiBotUltra.getConfig().getString("messages.botscout-kick");
        System.out.println(botScoutProtectionKickMsg);
        this.dnsblProtectionKickMsg = antiBotUltra.getConfig().getString("messages.dnsbl-kick");
        System.out.println(dnsblProtectionKickMsg);
        this.stopForumSpamAsnKickMsg = antiBotUltra.getConfig().getString("messages.stopforumspam.asn-kick");
        System.out.println(stopForumSpamAsnKickMsg);
        this.stopForumSpamCountryKickMsg = antiBotUltra.getConfig().getString("messages.stopforumspam.country-kick");
        System.out.println(stopForumSpamCountryKickMsg);
        this.stopForumSpamProxyKickMsg = antiBotUltra.getConfig().getString("messages.stopforumspam.proxy-kick");
        System.out.println(stopForumSpamProxyKickMsg);

        this.alternativeProxyEnabled = antiBotUltra.getConfig().getBoolean("protections.alternative.enabled");
        System.out.println(alternativeProxyEnabled);
        this.botProtectionEnabled = antiBotUltra.getConfig().getBoolean("protections.bot-protection.enabled");
        System.out.println(botProtectionEnabled);
        this.botScoutProtectionEnabled = antiBotUltra.getConfig().getBoolean("protections.botscout.proxy-check");
        System.out.println(botScoutProtectionEnabled);
        this.dnsblProtectionEnabled = antiBotUltra.getConfig().getBoolean("protections.dnsbl.enabled");
        System.out.println(dnsblProtectionEnabled);
        this.stopForumSpamAsnEnabled = antiBotUltra.getConfig().getBoolean("protections.stopforumspam.asn.enabled");
        System.out.println(stopForumSpamAsnEnabled);
        this.stopForumSpamCountryEnabled = antiBotUltra.getConfig().getBoolean("protections.stopforumspam.country.enabled");
        System.out.println(stopForumSpamCountryEnabled);
        this.stopForumSpamProxyEnabled = antiBotUltra.getConfig().getBoolean("protections.stopforumspam.proxy-check");
        System.out.println(stopForumSpamProxyEnabled);

        this.requiredPlayTime = antiBotUltra.getConfig().getInt("protections.bot-protection.advanced.play-time");
        System.out.println(requiredPlayTime);

        this.localBlacklistEnabled = antiBotUltra.getConfig().getBoolean("protections.local-blacklist");
        System.out.println(localBlacklistEnabled);
        this.localBlacklistKickMsg = antiBotUltra.getConfig().getString("messages.local-blacklist-kick");
        System.out.println(localBlacklistKickMsg);
    }

    @EventHandler
    public void stopForumSpamListener(AsyncPlayerPreLoginEvent event) {
        String playerIp = event.getAddress().getHostAddress();
        String playerName = event.getName();
        UUID playerUUID = event.getUniqueId();
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            return;
        }
        if (stopForumSpamAsnEnabled || stopForumSpamCountryEnabled) {
            stopForumSpamObject = stopForumSpamProtection.getDataForIpAsJson(playerIp);
            if (stopForumSpamAsnEnabled && stopForumSpamProtection.isAsnBlacklisted(stopForumSpamObject.get("asn").toString())) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&',
                        stopForumSpamAsnKickMsg)
                        .replace("%asn%", stopForumSpamObject.get("asn").toString())
                        .replace("%uuid%", playerUUID.toString())
                        .replace("%player%", playerName)
                        .replace("%ip%", playerIp));
            }
            if (stopForumSpamCountryEnabled && stopForumSpamProtection.isCountryBlacklisted(stopForumSpamObject.get("country").toString().replace("\"", ""))) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&',
                        stopForumSpamCountryKickMsg)
                        .replace("%country%", stopForumSpamObject.get("country").toString())
                        .replace("%uuid%", playerUUID.toString())
                        .replace("%player%", playerName)
                        .replace("%ip%", playerIp));
            }
        }
        if (stopForumSpamProxyEnabled && stopForumSpamProtection.isStopForumSpamBlacklisted(playerIp)) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&',
                    stopForumSpamProxyKickMsg)
                    .replace("%uuid%", playerUUID.toString())
                    .replace("%player%", playerName)
                    .replace("%ip%", playerIp));
            if (localBlacklistEnabled) {
                dataSource.blacklistPlayerIp(playerIp);
            }
        }
    }

    @EventHandler
    public void blacklistListener(AsyncPlayerPreLoginEvent event) {
        String playerIp = event.getAddress().getHostAddress();
        String playerName = event.getName();
        UUID playerUUID = event.getUniqueId();
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            return;
        }
        if (localBlacklistEnabled && dataSource.isPlayerBlacklistedIp(playerIp)) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&',
                    localBlacklistKickMsg)
                    .replace("%uuid%", playerUUID.toString())
                    .replace("%player%", playerName)
                    .replace("%ip%", playerIp));
        }
    }

    @EventHandler
    public void alternativeListener(AsyncPlayerPreLoginEvent event) {
        String playerIp = event.getAddress().getHostAddress();
        String playerName = event.getName();
        UUID playerUUID = event.getUniqueId();
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            return;
        }
        if (alternativeProxyEnabled && alternativeProxyProtection.isAlternativeProxiesBlacklisted(playerIp)) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&',
                    alternativeProxyKickMsg)
                    .replace("%uuid%", playerUUID.toString())
                    .replace("%player%", playerName)
                    .replace("%ip%", playerIp));
        }
    }

    @EventHandler
    public void botScoutListener(AsyncPlayerPreLoginEvent event) {
        String playerIp = event.getAddress().getHostAddress();
        String playerName = event.getName();
        UUID playerUUID = event.getUniqueId();
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            return;
        }
        if (botScoutProtectionEnabled && botScoutProtection.isBotScoutBlacklisted(playerIp)) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&',
                    botScoutProtectionKickMsg)
                    .replace("%uuid%", playerUUID.toString())
                    .replace("%player%", playerName)
                    .replace("%ip%", playerIp));
            if (localBlacklistEnabled) {
                dataSource.blacklistPlayerIp(playerIp);
            }
        }
    }

    @EventHandler
    public void dnsblListener(AsyncPlayerPreLoginEvent event) {
        String playerIp = event.getAddress().getHostAddress();
        String playerName = event.getName();
        UUID playerUUID = event.getUniqueId();
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            return;
        }
        if (dnsblProtectionEnabled && dnsblProtection.isBlocked(playerIp)) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&',
                    dnsblProtectionKickMsg)
                    .replace("%uuid%", playerUUID.toString())
                    .replace("%player%", playerName)
                    .replace("%ip%", playerIp));
            if (localBlacklistEnabled) {
                dataSource.blacklistPlayerIp(playerIp);
            }
        }
    }

    @EventHandler
    public void botProtectionListener(AsyncPlayerPreLoginEvent event) {
        String playerIp = event.getAddress().getHostAddress();
        String playerName = event.getName();
        UUID playerUUID = event.getUniqueId();
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            return;
        }
        if (botProtectionEnabled && !botProtection.isAllowed() && !dataSource.isPlayerWhitelisted(playerName)) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&',
                    botProtectionKickMsg.replace("%uuid%", playerUUID.toString())
                            .replace("%player%", playerName)
                            .replace("%ip%", playerIp)));
        }
    }

    @EventHandler
    public void botProtectionWhitelistListener(PlayerJoinEvent event) {
        String playerIp = event.getPlayer().getAddress().getAddress().getHostAddress();
        String playerName = event.getPlayer().getName();
        UUID playerUUID = event.getPlayer().getUniqueId();
        if (botProtectionEnabled && !dataSource.isPlayerWhitelisted(playerName)) {
            long playerTime = event.getPlayer().getStatistic(Statistic.PLAY_ONE_TICK) / 20;
            if (playerTime >= requiredPlayTime || event.getPlayer().hasPermission("antibot-ultra.whitelist")) {
                dataSource.whitelistPlayer(playerName);
                Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&',
                        botProtectionWhitelistMsg.replace("%uuid%", playerUUID.toString())
                                .replace("%player%", playerName)
                                .replace("%ip%", playerIp)), "antibot-ultra.notify");
            }
        }
    }
}
