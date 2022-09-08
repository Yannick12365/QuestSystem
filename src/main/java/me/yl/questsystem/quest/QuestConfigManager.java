package me.yl.questsystem.quest;

import me.yl.questsystem.main;
import me.yl.questsystem.npc.NPC;
import me.yl.questsystem.npc.NPCManager;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
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

        for (String key:keyList){
            NPC npcFound = new NPCManager().checkForNPC(key);

            if (npcFound == null){
                return;
            }

            for (String subkey: Objects.requireNonNull(questFileConf.getConfigurationSection(key)).getKeys(false)) {
                ItemStack item = new ItemStack(Objects.requireNonNull(Material.getMaterial(Objects.requireNonNull(questFileConf.getString(key + "." + subkey + ".Item")))));
                int itemAmount = questFileConf.getInt(key+"."+subkey +".ItemAmount");
                double reward = questFileConf.getDouble(key+"."+subkey+ ".Reward");
                int questid = Integer.parseInt(subkey);
                boolean active = questFileConf.getBoolean(key + "." +subkey +".Active");

                questArrayList.add(new Quest(item,itemAmount,reward, npcFound,questid, active));
            }
            new QuestManager().getQuestList().put(npcFound, questArrayList);
        }
    }

    public void writeQuesttConfig(Quest q){
        questFileConf.set(q.getNpc().getName()+"."+ q.getQuestID()+ ".Item",q.getItem().getType().toString());
        questFileConf.set(q.getNpc().getName()+"."+ q.getQuestID()+ ".ItemAmount",q.getItemAmount());
        questFileConf.set(q.getNpc().getName()+"."+ q.getQuestID()+ ".Reward",q.getReward());
        questFileConf.set(q.getNpc().getName()+"."+ q.getQuestID()+ ".Active",q.getActive());

        try {
            questFileConf.save(questFile);
            ArrayList<Quest> ql = new QuestManager().getNPCQuests(q.getNpc().getName());
            ql.add(q);
            new QuestManager().getQuestList().put(q.getNpc(), ql);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateActiveQuestConfig(Quest q){
        questFileConf.set(q.getNpc().getName()+"."+ q.getQuestID()+ ".Active",q.getActive());
        try {
            questFileConf.save(questFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeQuestConfig(Quest q){
        questFileConf.set(q.getNpc().getName()+"."+ q.getQuestID(), null);
        try {
            questFileConf.save(questFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateQuestConfig(Quest q){
        questFileConf.set(q.getNpc().getName()+"."+ q.getQuestID()+ ".ItemAmount",q.getItemAmount());
        questFileConf.set(q.getNpc().getName()+"."+ q.getQuestID()+ ".Reward",q.getReward());
        try {
            questFileConf.save(questFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
