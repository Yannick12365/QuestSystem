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
}