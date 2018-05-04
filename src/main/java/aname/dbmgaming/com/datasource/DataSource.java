package aname.dbmgaming.com.datasource;

public interface DataSource {

    void whitelistPlayer(String playerName);

    void unWhitelistPlayer(String playerName);

    boolean isPlayerWhitelisted(String playerName);

    void blacklistPlayerIp(String playerIp);

    void unBlacklistPlayerIp(String playerIp);

    boolean isPlayerBlacklistedIp(String playerIp);

    void purgePlayerBlacklistIp();

    void reload();

    void close();

    void save();

}
