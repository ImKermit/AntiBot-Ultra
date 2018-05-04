package aname.dbmgaming.com.commands;

import aname.dbmgaming.com.AntiBotUltra;
import aname.dbmgaming.com.protection.BotProtection;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AntiBotUltraCommands implements CommandExecutor {

    private final String PLUGIN_NAME = "&d[AntiBot-Ultra] ";
    private final String WHITELIST_COMMAND = PLUGIN_NAME + "&7-> /antibot-ultra &ewhitelist &7<&fplayer&7> / <&fplayer player1 player2 player3&7>";
    private final String UN_WHITELIST_COMMAND = PLUGIN_NAME + "&7-> /antibot-ultra &eun-whitelist &7<&fplayer&7> / <&fplayer player1 player2 player3&7>";
    private final String BLACKLIST_COMMAND = PLUGIN_NAME + "&7-> /antibot-ultra &eblacklist &7<&fip&7> / <&fip ip1 ip2 ip3&7>";
    private final String UN_BLACKLIST_COMMAND = PLUGIN_NAME + "&7-> /antibot-ultra &eun-blacklist &7<&fip&7> / <&fip ip1 ip2 ip3&7>";
    private final String PURGE_BLACKLIST = PLUGIN_NAME + "&7-> /antibot-ultra &epurge-blacklist";
    private final String RELOAD_COMMAND = PLUGIN_NAME + "&7-> /antibot-ultra &ereload";
    private final String START_PROTECTION_COMMAND = PLUGIN_NAME + "&7-> /antibot-ultra &estart-protection";
    private final String STOP_PROTECTION_COMMAND = PLUGIN_NAME + "&7-> /antibot-ultra &estop-protection";

    private final String PLAYER_WHITELISTED_MSG = PLUGIN_NAME + "&7-> &f%player% &7added to the whitelist !";
    private final String PLAYER_UN_WHITELISTED_MSG = PLUGIN_NAME + "&7-> &f%player% &7removed from the whitelist !";
    private final String IP_BLACKLISTED_MSG = PLUGIN_NAME + "&7-> &f%ip% &7added to the blacklist !";
    private final String IP_UN_BLACKLISTED_MSG = PLUGIN_NAME + "&7-> &f%ip% &7removed from the blacklist !";
    private final String BLACKLIST_INVALID_IP_MSG = PLUGIN_NAME + "&7-> &%fip% &7is invalid !";
    private final String BLACKLIST_PURGED_MSG = PLUGIN_NAME + "&7-> blacklist purged !";
    private final String RELOADED_MSG = PLUGIN_NAME + "&7-> database and config reloaded !";
    private final String PROTECTION_STARTED_MSG = PLUGIN_NAME + "&7-> protection started !";
    private final String PROTECTION_IS_DISABLED_MSG = PLUGIN_NAME + "&7-> protection is disabled (failed) !";
    private final String PROTECTION_STOPPED_MSG = PLUGIN_NAME + "&7-> protection stopped !";
    private final String PROTECTION_NOT_ACTIVE_MSG = PLUGIN_NAME + "&7-> protection not active !";

    private final String IP_REGEX_PATTERN = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";

    private final String NO_PERMISSION_MSG = PLUGIN_NAME + "&7-> &cYou don't have the required permission to run this command !";

    private final AntiBotUltra antiBotUltra;

    public AntiBotUltraCommands(AntiBotUltra antiBotUltra) {
        this.antiBotUltra = antiBotUltra;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (command.getName().equals("antibot-ultra")) {
            if (args.length == 0) {
                sendHelpPage(sender);
                return true;
            }
            if (args.length > 0) {
                if (sender.hasPermission("antibot-ultra.cmd.help")) {
                    if (args[0].equals("help")) {
                        sendHelpPage(sender);
                        return true;
                    }
                }
            }
            if (args.length > 0) {
                if (args[0].equals("whitelist")) {
                    if (args.length > 1) {
                        if (sender.hasPermission("antibot-ultra.cmd.whitelist")) {
                            for (int i = 1; i < args.length; i++) {
                                antiBotUltra.getDataSource().whitelistPlayer(args[i]);
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', PLAYER_WHITELISTED_MSG.replace("%player%", args[i])));
                                return true;
                            }
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', NO_PERMISSION_MSG));
                            return true;
                        }
                    }
                }
                if (args[0].equals("un-whitelist")) {
                    if (args.length > 1) {
                        if (sender.hasPermission("antibot-ultra.cmd.un-whitelist")) {
                            for (int i = 1; i < args.length; i++) {
                                antiBotUltra.getDataSource().unWhitelistPlayer(args[i]);
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', PLAYER_UN_WHITELISTED_MSG.replace("%player%", args[i])));
                                return true;
                            }
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', NO_PERMISSION_MSG));
                            return true;
                        }
                    }
                }
                if (args[0].equals("blacklist")) {
                    if (args.length > 1) {
                        if (sender.hasPermission("antibot-ultra.cmd.blacklist")) {
                            Pattern IP_PATTERN = Pattern.compile(IP_REGEX_PATTERN);
                            for (int i = 1; i < args.length; i++) {
                                Matcher matcher = IP_PATTERN.matcher(args[i]);
                                boolean lineContainsIP = matcher.find();
                                if (lineContainsIP) {
                                    antiBotUltra.getDataSource().blacklistPlayerIp(args[i]);
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', IP_BLACKLISTED_MSG.replace("%ip%", args[i])));
                                    return true;
                                } else {
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', BLACKLIST_INVALID_IP_MSG.replace("%ip%", args[i])));
                                    return true;
                                }
                            }
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', NO_PERMISSION_MSG));
                            return true;
                        }
                    }
                }
                if (args[0].equals("un-blacklist")) {
                    if (args.length > 1) {
                        if (sender.hasPermission("antibot-ultra.cmd.un-blacklist")) {
                            Pattern IP_PATTERN = Pattern.compile(IP_REGEX_PATTERN);
                            for (int i = 1; i < args.length; i++) {
                                Matcher matcher = IP_PATTERN.matcher(args[i]);
                                boolean lineContainsIP = matcher.find();
                                if (lineContainsIP) {
                                    antiBotUltra.getDataSource().unBlacklistPlayerIp(args[i]);
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', IP_UN_BLACKLISTED_MSG.replace("%ip%", args[i])));
                                    return true;
                                } else {
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', BLACKLIST_INVALID_IP_MSG.replace("%ip%", args[i])));
                                    return true;
                                }
                            }
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', NO_PERMISSION_MSG));
                            return true;
                        }
                    }
                }
                if (args[0].equals("purge-blacklist")) {
                    if (sender.hasPermission("antibot-ultra.cmd.purge-blacklist")) {
                        antiBotUltra.getDataSource().purgePlayerBlacklistIp();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', BLACKLIST_PURGED_MSG));
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', NO_PERMISSION_MSG));
                        return true;
                    }
                }
                if (args[0].equals("reload")) {
                    if (sender.hasPermission("antibot-ultra.cmd.reload")) {
                        antiBotUltra.getDataSource().reload();
                        antiBotUltra.getCustomConfig().reloadConfig();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', RELOADED_MSG));
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', NO_PERMISSION_MSG));
                        return true;
                    }
                }
                if (args[0].equals("start-protection")) {
                    if (sender.hasPermission("antibot-ultra.cmd.start-protection")) {
                        if (antiBotUltra.getBotProtection().getStatus() != BotProtection.Status.DISABLED) {
                            antiBotUltra.getBotProtection().start();
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', PROTECTION_STARTED_MSG));
                            return true;
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', PROTECTION_IS_DISABLED_MSG));
                            return true;
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', NO_PERMISSION_MSG));
                        return true;
                    }
                }
                if (args[0].equals("stop-protection")) {
                    if (sender.hasPermission("antibot-ultra.cmd.start-protection")) {
                        if (antiBotUltra.getBotProtection().getStatus() != BotProtection.Status.DISABLED) {
                            if (antiBotUltra.getBotProtection().getStatus() == BotProtection.Status.ACTIVE) {
                                antiBotUltra.getBotProtection().stop();
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', PROTECTION_STOPPED_MSG));
                                return true;
                            } else {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', PROTECTION_NOT_ACTIVE_MSG));
                                return true;
                            }
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', PROTECTION_IS_DISABLED_MSG));
                            return true;
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', NO_PERMISSION_MSG));
                        return true;
                    }
                }
            }
        }
        return true;
    }

    public void sendHelpPage(CommandSender sender) {
        if (sender.hasPermission("antibot-ultra.cmd.help")) {
            sender.sendMessage("&c==============================");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', WHITELIST_COMMAND));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', UN_WHITELIST_COMMAND));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', BLACKLIST_COMMAND));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', UN_BLACKLIST_COMMAND));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', PURGE_BLACKLIST));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', RELOAD_COMMAND));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', START_PROTECTION_COMMAND));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', STOP_PROTECTION_COMMAND));
            sender.sendMessage("&c==============================");
        }
    }

}
