package me.yl.questsystem.manager;

import me.yl.questsystem.main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class SettingsConfigManager {
    public static File settingsFile;
    public static FileConfiguration settingsFileConf;

    public void createConfigConfiguration(main main){
        if (!main.getDataFolder().exists()){
            main.getDataFolder().mkdir();
        }
        settingsFile = new File(main.getDataFolder(), "settings.yml");
        if (!settingsFile.exists()){
            main.saveResource("settings.yml",false);
        }
        settingsFileConf = YamlConfiguration.loadConfiguration(settingsFile);
    }

    public void writeQuestPacketNumber(int i){
        settingsFileConf.set("QuestPacketNr",i);
        try {
            settingsFileConf.save(settingsFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int readQuestPacketNumber(){
        return (Integer) settingsFileConf.get("QuestPacketNr");
    }
}
