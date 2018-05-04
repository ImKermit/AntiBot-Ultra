package aname.dbmgaming.com.datasource;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class YamlDataSource implements DataSource {


    //TODO: FIX DATABASE!

    private FileConfiguration customConfig;
    private File customConfigFile;

    public YamlDataSource() {
        customConfigFile = new File("./plugins/AntiBot-Ultra/database.yml");
        reload();
    }

    @Override
    public void whitelistPlayer(String playerName) {
        List<String> whitelistList = customConfig.getStringList("whitelist");
        if (!whitelistList.contains(playerName)) {
            whitelistList.add(playerName);
            customConfig.set("whitelist", whitelistList);
            save();
        }
    }

    @Override
    public void unWhitelistPlayer(String playerName) {
        List<String> whitelistList = customConfig.getStringList("whitelist");
        if (whitelistList.contains(playerName)) {
            whitelistList.remove(playerName);
            customConfig.set("whitelist", whitelistList);
            save();
        }
    }

    @Override
    public boolean isPlayerWhitelisted(String playerName) {
        return customConfig.getStringList("whitelist").contains(playerName);
    }

    @Override
    public void blacklistPlayerIp(String playerIp) {
        List<String> blacklistList = customConfig.getStringList("blacklist");
        if (!blacklistList.contains(playerIp)) {
            blacklistList.add(playerIp);
            customConfig.set("blacklist", blacklistList);
            save();
        }
    }

    @Override
    public void unBlacklistPlayerIp(String playerIp) {
        List<String> blacklistList = customConfig.getStringList("blacklist");
        if (blacklistList.contains(playerIp)) {
            blacklistList.remove(playerIp);
            customConfig.set("blacklist", blacklistList);
            save();
        }
    }

    @Override
    public void purgePlayerBlacklistIp() {
        List<String> blacklistList = customConfig.getStringList("blacklist");
        blacklistList.clear();
        customConfig.set("blacklist", blacklistList);
        save();
    }

    @Override
    public boolean isPlayerBlacklistedIp(String playerIp) {
        return customConfig.getStringList("blacklist").contains(playerIp);
    }

    @Override
    public void reload() {
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
        save();
    }

    @Override
    public void close() {

    }

    @Override
    public void save() {
        try {
            customConfig.save(customConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
