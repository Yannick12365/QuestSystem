package me.yl.questsystem.quest;

import me.yl.questsystem.npc.NPC;
import me.yl.questsystem.npc.NPCManager;
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


}