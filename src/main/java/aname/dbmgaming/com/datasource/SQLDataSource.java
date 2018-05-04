package aname.dbmgaming.com.datasource;

import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

public abstract class SQLDataSource implements DataSource {

    protected Connection connection;

    @Override
    public abstract void close();

    protected abstract void connect() throws ClassNotFoundException, SQLException;

    protected abstract void setup() throws SQLException;

    @Override
    public synchronized void whitelistPlayer(String playerName) {
        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement("insert into whitelist (player_name)" + " values (?);");
            pst.setString(1, playerName);
            pst.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.WARNING, "[AntiBot-Ultra] Failed to whitelist player " + playerName, e);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    @Override
    public synchronized void unWhitelistPlayer(String playerName) {
        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement("delete from whitelist where player_name = ?;");
            pst.setString(1, playerName);
            pst.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.WARNING, "[AntiBot-Ultra] Failed to unwhitelist player " + playerName, e);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    @Override
    public synchronized boolean isPlayerWhitelisted(String playerName) {
        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement("select player_name from whitelist where player_name = ?;");
            pst.setString(1, playerName);
            return pst.executeQuery().next();
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.WARNING, "[AntiBot-Ultra] Failed to determine if player " + playerName + " is whitelisted!", e);
            return false;
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    @Override
    public synchronized void blacklistPlayerIp(String playerIp) {
        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement("insert into blacklist (player_ip) values (?);");
            pst.setString(1, playerIp);
            pst.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.WARNING, "[AntiBot-Ultra] Failed to blacklist player " + playerIp, e);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    @Override
    public synchronized void unBlacklistPlayerIp(String playerIp) {
        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement("delete from blacklist where player_name = ?;");
            pst.setString(1, playerIp);
            pst.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.WARNING, "[AntiBot-Ultra] Failed to unblacklist player " + playerIp, e);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    @Override
    public synchronized boolean isPlayerBlacklistedIp(String playerIp) {
        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement("select player_ip from blacklist where player_ip = ?;");
            pst.setString(1, playerIp);
            return pst.executeQuery().next();
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.WARNING, "[AntiBot-Ultra] Failed to determine if player " + playerIp + " is blacklisted!", e);
            return false;
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    @Override
    public synchronized void purgePlayerBlacklistIp() {
        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement("delete from blacklist");
            pst.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.WARNING, "[AntiBot-Ultra] Failed to purge blacklist!", e);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
