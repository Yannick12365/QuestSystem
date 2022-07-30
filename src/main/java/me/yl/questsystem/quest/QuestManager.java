package me.yl.questsystem.quest;

import me.yl.questsystem.npc.NPCManager;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestManager {


    private static final HashMap<NPCManager, ArrayList<Quest>> questList =  new HashMap<>();



    public HashMap<NPCManager, ArrayList<Quest>> getQuestList() {
        return questList;
    }



}
