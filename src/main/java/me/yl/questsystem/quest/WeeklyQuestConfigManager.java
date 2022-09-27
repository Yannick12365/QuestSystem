package me.yl.questsystem.quest;

import me.yl.questsystem.main;
import me.yl.questsystem.manager.SettingsConfigManager;
import me.yl.questsystem.npc.NPC;
import me.yl.questsystem.npc.NPCManager;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class WeeklyQuestConfigManager {

    public static File questFile;
    public static FileConfiguration questFileConf;

    public void createConfigConfiguration(main main){
        if (!main.getDataFolder().exists()){
            main.getDataFolder().mkdir();
        }
        questFile = new File(main.getDataFolder(), "weeklyQuest.yml");
        if (!questFile.exists()){
            main.saveResource("weeklyQuest.yml",false);
        }
        questFileConf = YamlConfiguration.loadConfiguration(questFile);
    }

    public void readWeeklyQuestConfig(){
        Set<String> keyList =  questFileConf.getKeys(false);
        HashMap<NPC, ArrayList<Quest>> weeklyQuests = new HashMap<>();
        for (String key:keyList) {
            ArrayList<Quest> questList = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
              Quest q = new QuestManager().getNPCQuestByID(key,(int) questFileConf.get(key+".quest" + i + ".id"), new SettingsConfigManager().readQuestPacketNumber());
              q.setItemAmount( (int) questFileConf.get(key+".quest" + i + ".amount"));
              questList.add(q);
            }
            weeklyQuests.put(new NPCManager().checkForNPC(key), questList);
        }
        QuestManager.setWeeklyQuests(weeklyQuests);
    }

    public void writeWeeklyQuesttConfig(){
        HashMap<NPC, ArrayList<Quest>> weeklyQuests = new QuestManager().chooseWeeklyQuests();
        for (NPC n:new NPCManager().getNPClist()) {
            ArrayList<Quest> temp = weeklyQuests.get(n);
            if (temp != null) {
                int counter = 0;
                for (Quest q : temp) {
                    questFileConf.set(n.getName() + ".quest" + counter + ".id", q.getQuestID());
                    questFileConf.set(n.getName() + ".quest" + counter + ".amount", q.getItemAmount());
                    counter++;
                }
            }
        }
        try {
            questFileConf.save(questFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearWeeklyQuesttConfig(){
        for(String key : questFileConf.getKeys(false)) {
            questFileConf.set(key,null);
        }
        try {
            questFileConf.save(questFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
