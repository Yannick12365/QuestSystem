package me.yl.questsystem.quest;

import me.yl.questsystem.npc.NPC;
import me.yl.questsystem.npc.NPCManager;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestManager {


    private static final HashMap<NPC, ArrayList<Quest>> questList =  new HashMap<>();

    public HashMap<NPC, ArrayList<Quest>> getQuestList() {
        return questList;
    }

    public int countNPCQuests(String n){
        if (questList.get(n) == null) {
            return 0;
        }
        return questList.get(n).size();
    }

    public Quest getNPCQuestByID(String n,int id){
        if(questList.get(n) != null) {
            ArrayList<Quest> quests = questList.get(n);
            for (Quest q : quests) {
                if (q.getQuestID() == id) {
                    return q;
                }
            }
        }
        return null;
    }

    public ArrayList<Quest> getNPCQuests(String n){
        if (questList.get(n) != null) {
            return questList.get(n);
        }
        return null;
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
}