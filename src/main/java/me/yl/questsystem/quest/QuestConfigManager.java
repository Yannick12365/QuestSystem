package me.yl.questsystem.quest;

import me.yl.questsystem.main;
import me.yl.questsystem.npc.NPC;
import me.yl.questsystem.npc.NPCManager;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

public class QuestConfigManager {
    public static File questFile;
    public static FileConfiguration questFileConf;

    public void createConfigConfiguration(main main){
        if (!main.getDataFolder().exists()){
            main.getDataFolder().mkdir();
        }
        questFile = new File(main.getDataFolder(), "quest.yml");
        if (!questFile.exists()){
            main.saveResource("quest.yml",false);
        }
        questFileConf = YamlConfiguration.loadConfiguration(questFile);
    }

    public void readQuestConfig(){
        Set<String> keyList =  questFileConf.getKeys(false);
        ArrayList<Quest> questArrayList = new ArrayList<>();
        NPC npcFound = null;

        for (String key:keyList){
            for(NPC npc:new NPCManager().getNPClist()){
                if (npc.getName(npc) == key){
                    npcFound = npc;
                    break;
                }
            }
            if (npcFound == null){
                return;
            }

            for (String subkey: questFileConf.getConfigurationSection(key).getKeys(false)) {
                ItemStack item = questFileConf.getConfigurationSection(key+"."+subkey).getItemStack("Item");
                int itemAmount = questFileConf.getConfigurationSection(key+"."+subkey).getInt("ItemAmount");
                double reward = questFileConf.getConfigurationSection(key+"."+subkey).getDouble("Reward");
                int questid = Integer.parseInt(questFileConf.getConfigurationSection(key+"."+subkey).toString());

                questArrayList.add(new Quest(item,itemAmount,reward, npcFound,questid));
            }
            new QuestManager().getQuestList().put(npcFound, questArrayList);
        }
    }
}
