package aname.dbmgaming.com.config;

import aname.dbmgaming.com.AntiBotUltra;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.logging.Level;

public class CustomConfig {

    private AntiBotUltra antiBotUltra;

    private FileConfiguration customConfig = null;
    private File customConfigFile = null;

    public CustomConfig(AntiBotUltra antiBotUltra) {
        this.antiBotUltra = antiBotUltra;
    }

    public void reloadConfig() {
        if (customConfigFile == null) {
            customConfigFile = new File(antiBotUltra.getDataFolder(), "config.yml");
        }
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

        // Look for defaults in the jar
        Reader defConfigStream = null;
        try {
            defConfigStream = new InputStreamReader(antiBotUltra.getResource("config.yml"), "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            customConfig.setDefaults(defConfig);
        }
    }

    public FileConfiguration getCustomConfig() {
        if (customConfig == null) {
            reloadConfig();
        }
        return customConfig;
    }

    public void saveConfig() {
        if (customConfig == null || customConfigFile == null) {
            return;
        }
        try {
            getCustomConfig().save(customConfigFile);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "[AntiBot-Ultra] Could not save config to " + customConfigFile, ex);
        }
    }

    public void saveDefaultConfig() {
        if (customConfigFile == null) {
            customConfigFile = new File(antiBotUltra.getDataFolder(), "config.yml");
        }
        if (!customConfigFile.exists()) {
            antiBotUltra.saveResource("config.yml", false);
        }
    }
}
