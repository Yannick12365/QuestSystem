package me.yl.questsystem.quest;

import me.yl.questsystem.manager.SettingsConfigManager;
import me.yl.questsystem.npc.NPC;
import me.yl.questsystem.npc.NPCManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class QuestManager {
    private static final HashMap<NPC, ArrayList<Quest>> activeQuestPacket =  new HashMap<>();
    private static HashMap<NPC, ArrayList<Quest>> weeklyQuestList =  new HashMap<>();

    public static HashMap<NPC, ArrayList<Quest>> getActiveQuestPacket() {
        return activeQuestPacket;
    }



    public ArrayList<Quest> getNPCQuestsFromPacket(NPC npc, int nr){
        return new QuestConfigManager().readNPCQuestsFromPacket(npc, nr);
    }

    public int getLatestQuestID(NPC npc){
        int id = 0;

        for (int i = 0; i < 3; i++) {
            if (getNPCQuestsFromPacket(npc, i) != null) {
                for (Quest q : getNPCQuestsFromPacket(npc, i)) {
                    if (id < q.getQuestID()) {
                        id = q.getQuestID();
                    }
                }
            }
        }
        return id;
    }

    public Quest getNPCQuestByID(String n,int id,int nr){
        NPC npcFound = new NPCManager().checkForNPC(n);

        if (npcFound != null) {
            ArrayList<Quest> tempList = getNPCQuestsFromPacket(npcFound, nr);
            if (tempList != null) {
                for (Quest q : tempList) {
                    if (q.getQuestID() == id) {
                        return q;
                    }
                }
            }
        }
        return null;
    }

    public ArrayList<Quest> getNPCQuests(String n, int nr) {
        NPC npcFound = new NPCManager().checkForNPC(n);

        if (npcFound != null){
            ArrayList<Quest> tempList = getNPCQuestsFromPacket(npcFound, nr);
            if (tempList != null) {
                return tempList;
            }
        }
        return new ArrayList<Quest>();
    }

    public void changeActiveStatus(boolean active, String n, int questID, int nr, Player p){
        Quest q = getNPCQuestByID(n, questID, nr);
        NPC npcFound = new NPCManager().checkForNPC(n);

        ArrayList<Integer>tmp = new WeeklyQuestConfigManager().getIDsFromNPCsWeeklys(npcFound);
        if (tmp != null) {
            for (int id : tmp) {
                if (id == q.getQuestID()) {
                    p.sendMessage("Diese Quest kann nicht deaktiviert werden, da sie in der momentanen Woche, als Quest angeboten wird!");
                    return;
                }
            }
        }

        ArrayList<Quest> ql = new ArrayList<>();

        for(Quest tempQ:getNPCQuests(n,nr)) {
            if (tempQ.getQuestID() == questID){
                q.setActive(active);
                ql.add(q);
            }else {
                ql.add(tempQ);
            }
        }
        if (nr == new SettingsConfigManager().readQuestPacketNumber()) {
            activeQuestPacket.put(npcFound, ql);
        }

        new QuestConfigManager().updateActiveQuestConfig(q, nr);
    }

    public void removeQuest(Quest q, NPC npc, int nr, Player p){
        ArrayList<Integer>tmp = new WeeklyQuestConfigManager().getIDsFromNPCsWeeklys(npc);
        if (tmp != null) {
            for (int id : tmp) {
                if (id == q.getQuestID()) {
                    p.sendMessage("Diese Quest kann nicht gelöscht werden, da sie in der momentanen Woche, als Quest angeboten wird!");
                    return;
                }
            }
        }
        if (nr == new SettingsConfigManager().readQuestPacketNumber()) {
            activeQuestPacket.get(npc).remove(q);
        }
        new QuestConfigManager().removeQuestConfig(q, nr);
        p.sendMessage("§c§lQuest erfolgreich gelöscht!");
    }

    public void changeQuestAmounts(NPC npc, Quest q, int amount, double reward, int nr){
        ArrayList<Quest> ql = new ArrayList<>();

        for(Quest tempQ: getNPCQuestsFromPacket(npc,nr)) {
            if (tempQ.getQuestID() == q.getQuestID()){
                q.setItemAmount(amount);
                q.setReward(reward);
                ql.add(q);
            }else {
                ql.add(tempQ);
            }
        }
        if (nr == new SettingsConfigManager().readQuestPacketNumber()) {
            activeQuestPacket.put(npc, ql);
        }
        new QuestConfigManager().updateQuestConfig(q, nr);
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

    public int getEditQuestGUISize(NPC npc, int nr){
        int questCount = (new QuestManager().countNPCQuests(npc.getName(), nr));
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

    public int countNPCQuests(String n, int nr) {
        NPC npcFound = new NPCManager().checkForNPC(n);

        if (npcFound != null) {
            ArrayList<Quest> tempList = getNPCQuestsFromPacket(npcFound, nr);
            if (tempList != null) {
                return tempList.size();
            }
        }
        return 0;
    }






    public HashMap<NPC, ArrayList<Quest>> chooseWeeklyQuests(){
        HashMap<NPC, ArrayList<Quest>> weeklyQuests = new HashMap<>();
        for (NPC n:new NPCManager().getNPClist()) {
            if (!new QuestManager().checkForFiveActiveQuests(n)){
                continue;
            }
            ArrayList<Quest> temp = activeQuestPacket.get(n);

            if (temp != null) {
                Collections.shuffle(temp);
                ArrayList<Quest> tempWeekly = new ArrayList();
                for (int i = 0; i < 5; i++) {
                    if (!temp.get(i).getActive()){
                        i--;
                        continue;
                    }
                    tempWeekly.add(setAmountDifference(temp.get(i)));
                }
                weeklyQuests.put(n, tempWeekly);
            }
        }
        return weeklyQuests;
    }

    public Quest setAmountDifference(Quest q){


        return q;
    }
    public static void setWeeklyQuests(HashMap<NPC, ArrayList<Quest>> tempList){
        weeklyQuestList = tempList;
    }

    public boolean checkForFiveActiveQuests(NPC npc){
        ArrayList<Quest> tmp = activeQuestPacket.get(npc);
        int counter = 0;
        for (Quest q : tmp){
            if (q.getActive()){
                counter++;
                if (counter == 5){
                    return true;
                }
            }
        }
        return false;
    }
}
