package me.yl.questsystem.quest;

import me.yl.questsystem.npc.NPC;
import me.yl.questsystem.npc.NPCManager;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestManager {


    private static final HashMap<NPC, ArrayList<Quest>> questList =  new HashMap<>();

    public HashMap<NPC, ArrayList<Quest>> getQuestList() {
        return questList;
    }

    public int countNPCQuests(String n) {
        NPC npcFound = null;
        for (NPC existNPC : new NPCManager().getNPClist()) {
            if (existNPC.getName().equalsIgnoreCase(n)) {
                npcFound = existNPC;
            }
        }
        if (npcFound != null) {
            if (questList.get(npcFound) != null) {
                return questList.get(npcFound).size();
            }
        }
        return 0;
    }

    public Quest getNPCQuestByID(String n,int id){
        NPC npcFound = null;
        for (NPC existNPC : new NPCManager().getNPClist()) {
            if (existNPC.getName().equalsIgnoreCase(n)) {
                npcFound = existNPC;
            }
        }
        if(questList.get(npcFound) != null) {
            ArrayList<Quest> quests = questList.get(npcFound);
            for (Quest q : quests) {
                if (q.getQuestID() == id) {
                    return q;
                }
            }
        }
        return null;
    }

    public ArrayList<Quest> getNPCQuests(String n) {

        NPC npcFound = null;
        for (NPC existNPC : new NPCManager().getNPClist()) {
            if (existNPC.getName().equalsIgnoreCase(n)) {
                npcFound = existNPC;
            }
        }
        if (npcFound != null) {
            if (questList.get(npcFound) != null) {
                return questList.get(npcFound);
            }
        }
        return new ArrayList<Quest>();
    }

    public boolean checkSignLoreMenge(String lore){
        return lore.matches("[1-9][0-9]*");
    }

    public boolean checkSignLorePreis(String lore){
        if(lore.matches("[0-9]+(\\.[0-9]*)?")){
            return true;
        } else if (lore.matches("[0-9]+(,[0-9]*)?")) {
            return true;
        }
        return false;
    }

    public void changeActiveStatus(boolean active, String n, int questID){
        Quest q = getNPCQuestByID(n, questID);
        NPC npcFound = null;
        for (NPC existNPC : new NPCManager().getNPClist()) {
            if (existNPC.getName().equalsIgnoreCase(n)) {
                npcFound = existNPC;
            }
        }
        ArrayList<Quest> ql = new ArrayList<>();

        for(Quest tempQ:questList.get(npcFound)) {
           if (tempQ.getQuestID() == questID){
               q.setActive(active);
               ql.add(q);
           }else {
               ql.add(tempQ);
           }
        }
        questList.put(npcFound, ql);

        new QuestConfigManager().updateActiveQuestConfig(q);
    }

    public int getEditQuestGUISize(NPC npc){

        int questCount = (new QuestManager().countNPCQuests(npc.getName()));
        int countSlots;
        if (questCount <= 21){
            countSlots = 54;
        }else{
            if (questCount % 21 == 0){
                countSlots = (questCount/21*54) - (questCount/21*9);
            }else{
                countSlots = (((questCount/21)+1)*54) - (((questCount/21)+1)*9);
            }
        }
        return countSlots;
    }
    public void removeQuest(Quest q, NPC npc){

        questList.get(npc).remove(q);
        new QuestConfigManager().removeQuestConfig(q);

    }

    public void changeQuestAmounts(NPC npc, Quest q, int amount, double reward){
        ArrayList<Quest> ql = new ArrayList<>();

        for(Quest tempQ:questList.get(npc)) {
            if (tempQ.getQuestID() == q.getQuestID()){
                q.setItemAmount(amount);
                q.setReward(reward);
                ql.add(q);
            }else {
                ql.add(tempQ);
            }
        }
        questList.put(npc, ql);
        new QuestConfigManager().updateQuestConfig(q);
    }
}