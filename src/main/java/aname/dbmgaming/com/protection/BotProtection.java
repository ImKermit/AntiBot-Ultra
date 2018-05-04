package aname.dbmgaming.com.protection;

import aname.dbmgaming.com.AntiBotUltra;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitTask;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class BotProtection {


    private final AntiBotUltra antiBotUltra;
    private Status status;

    private int flaggedConnections;
    private int duration;
    private int maxLogins;
    private int interval;

    private boolean botProtectionEnabled;

    private String permission;
    private String botProtectionActivatedMsg;
    private String botProtectionDeactivatedMsg;

    private Instant lastFlagged;
    private BukkitTask antiBotTask;

    public BotProtection(AntiBotUltra antiBotUltra) {
        this.antiBotUltra = antiBotUltra;
        this.duration = antiBotUltra.getConfig().getInt("protections.bot-protection.advanced.duration");
        this.maxLogins = antiBotUltra.getConfig().getInt("protections.bot-protection.advanced.max-logins");
        this.interval = antiBotUltra.getConfig().getInt("protections.bot-protection.advanced.interval");
        this.botProtectionEnabled = antiBotUltra.getConfig().getBoolean("protections.bot-protection.enabled");
        this.permission = "antibot-ultra.protection.notify";
        this.botProtectionActivatedMsg = antiBotUltra.getConfig().getString("messages.bot-protection.activated");
        this.botProtectionDeactivatedMsg = antiBotUltra.getConfig().getString("messages.bot-protection.deactivated");
        this.antiBotTask = null;
        this.flaggedConnections = 0;
        this.status = Status.DISABLED;
        this.prepare();
    }

    private void prepare() {
        stop();
        status = Status.DISABLED;
        if (!botProtectionEnabled) {
            return;
        }
        status = Status.WAITING;
    }

    public void start() {
        stop();
        status = Status.ACTIVE;

        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.hasPermission(permission))
                .forEach(player -> player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        botProtectionActivatedMsg.replace("%uuid%", player.getUniqueId().toString()).replace("%player%", player.getName()).replace("%time%", String.valueOf(duration * 20)))));
        antiBotTask = Bukkit.getScheduler().runTaskLater(antiBotUltra, this::stop, duration * 20);
    }

    public void stop() {
        if (status != Status.ACTIVE) {
            return;
        }
        status = Status.WAITING;

        antiBotTask.cancel();
        antiBotTask = null;

        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.hasPermission(permission))
                .forEach(player -> player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        botProtectionDeactivatedMsg.replace("%uuid%", player.getUniqueId().toString()).replace("%player%", player.getName()).replace("%time%", String.valueOf(duration * 20)))));
        flaggedConnections = 0;
    }

    public boolean isAllowed() {
        if (status == Status.DISABLED) {
            return true;
        } else if (status == Status.ACTIVE) {
            return false;
        }
        if (lastFlagged == null) {
            lastFlagged = Instant.now();
        }
        if (ChronoUnit.SECONDS.between(lastFlagged, Instant.now()) <= interval) {
            flaggedConnections++;
        } else {
            flaggedConnections = 1;
            lastFlagged = null;
        }
        if (flaggedConnections > maxLogins) {
            start();
            return false;
        }
        return true;
    }

    public Status getStatus() {
        return status;
    }

    public enum Status {
        ACTIVE, WAITING, DISABLED
    }

}
