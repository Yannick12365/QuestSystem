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
import java.util.Objects;
import java.util.Set;

public class QuestConfigManager {

    private static ArrayList<File> questFile = new ArrayList<>();

    private static ArrayList<FileConfiguration> questFileConf = new ArrayList<>();

    public void createConfigConfiguration(main main){
        if (!main.getDataFolder().exists()){
            main.getDataFolder().mkdir();
        }

        for (int i = 0;i<3;i++) {
            questFile.add(new File(main.getDataFolder(), "quest"+i+".yml"));
            if (!questFile.get(i).exists()) {
                main.saveResource("quest"+i+".yml", false);
            }
            questFileConf.add(YamlConfiguration.loadConfiguration(questFile.get(i)));
        }
    }

    public void readQuestConfig(int i){
        int nr;
        if (new SettingsConfigManager().readQuestPacketNumberQuest() == i){
            nr = i;
        }else {
            nr = new SettingsConfigManager().readQuestPacketNumberQuest();
        }

        Set<String> keyList =  questFileConf.get(nr).getKeys(false);


        for (String key:keyList){
            ArrayList<Quest> questArrayList = new ArrayList<>();
            NPC npcFound = new NPCManager().checkForNPC(key);

            if (npcFound == null){
                return;
            }

            for (String subkey: Objects.requireNonNull(questFileConf.get(nr).getConfigurationSection(key)).getKeys(false)) {
                ItemStack item = new ItemStack(Objects.requireNonNull(Material.getMaterial(Objects.requireNonNull(questFileConf.get(nr).getString(key + "." + subkey + ".Item")))));
                int itemAmount = questFileConf.get(nr).getInt(key+"."+subkey +".ItemAmount");
                double reward = questFileConf.get(nr).getDouble(key+"."+subkey+ ".Reward");
                int questid = Integer.parseInt(subkey);
                boolean active = questFileConf.get(nr).getBoolean(key + "." +subkey +".Active");

                questArrayList.add(new Quest(item,itemAmount,reward, npcFound,questid, active));
            }

            QuestManager.getActiveQuestPacket().put(npcFound, questArrayList);
        }
    }

    public ArrayList<Quest> readNPCQuestsFromPacket(NPC npc, int nr){
        ArrayList<Quest> tmpList = new ArrayList<>();
        try {
            for (String subkey : Objects.requireNonNull(questFileConf.get(nr).getConfigurationSection(npc.getName())).getKeys(false)) {
                ItemStack item = new ItemStack(Objects.requireNonNull(Material.getMaterial(Objects.requireNonNull(questFileConf.get(nr).getString(npc.getName() + "." + subkey + ".Item")))));
                int itemAmount = questFileConf.get(nr).getInt(npc.getName() + "." + subkey + ".ItemAmount");
                double reward = questFileConf.get(nr).getDouble(npc.getName() + "." + subkey + ".Reward");
                int questid = Integer.parseInt(subkey);
                boolean active = questFileConf.get(nr).getBoolean(npc.getName() + "." + subkey + ".Active");

                tmpList.add(new Quest(item, itemAmount, reward, npc, questid, active));
            }
        }catch (Exception e){
            return null;
        }
        return tmpList;
    }

    public void writeQuesttConfig(Quest q, int nr){
        questFileConf.get(nr).set(q.getNpc().getName()+"."+ q.getQuestID()+ ".Item",q.getItem().getType().toString());
        questFileConf.get(nr).set(q.getNpc().getName()+"."+ q.getQuestID()+ ".ItemAmount",q.getItemAmount());
        questFileConf.get(nr).set(q.getNpc().getName()+"."+ q.getQuestID()+ ".Reward",q.getReward());
        questFileConf.get(nr).set(q.getNpc().getName()+"."+ q.getQuestID()+ ".Active",q.getActive());

        try {
            questFileConf.get(nr).save(questFile.get(nr));

            if (nr == new SettingsConfigManager().readQuestPacketNumber()) {
                ArrayList<Quest> ql = new QuestManager().getNPCQuests(q.getNpc().getName(), nr);
                ql.add(q);
                QuestManager.getActiveQuestPacket().put(q.getNpc(), ql);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateActiveQuestConfig(Quest q, int nr){
        questFileConf.get(nr).set(q.getNpc().getName()+"."+ q.getQuestID()+ ".Active",q.getActive());
        try {
            questFileConf.get(nr).save(questFile.get(nr));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeQuestConfig(Quest q, int nr){
        questFileConf.get(nr).set(q.getNpc().getName()+"."+ q.getQuestID(), null);
        try {
            questFileConf.get(nr).save(questFile.get(nr));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateQuestConfig(Quest q, int nr){
        questFileConf.get(nr).set(q.getNpc().getName()+"."+ q.getQuestID()+ ".ItemAmount",q.getItemAmount());
        questFileConf.get(nr).set(q.getNpc().getName()+"."+ q.getQuestID()+ ".Reward",q.getReward());
        try {
            questFileConf.get(nr).save(questFile.get(nr));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
