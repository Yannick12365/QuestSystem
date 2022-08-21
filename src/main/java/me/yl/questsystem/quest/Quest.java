package me.yl.questsystem.quest;

import me.yl.questsystem.npc.NPC;
import org.bukkit.inventory.ItemStack;

public class Quest {

    private final ItemStack item;
    private int itemAmount;
    private double reward;
    private NPC npc;
    private final int questID;
    private boolean active;

    public Quest(ItemStack item, int itemAmount, double reward, NPC npc){
        this.item = item;
        this.itemAmount = itemAmount;
        this.reward = reward;
        this.npc = npc;
        questID = new QuestManager().getQuestList().get(npc).get(new QuestManager().getQuestList().get(npc).size()-1).questID+1;
        this.active = true;
    }

    public Quest(ItemStack item, int itemAmount, double reward, NPC npc, int questID, boolean active){
        this.item = item;
        this.itemAmount = itemAmount;
        this.reward = reward;
        this.npc = npc;
        this.questID = questID;
        this.active = active;
    }

    public ItemStack getItem() {
        return item;
    }
    public int getItemAmount() {
        return itemAmount;
    }
    public double getReward() {
        return reward;
    }
    public NPC getNpc() {
        return npc;
    }
    public void setNpc(NPC npc) {
        this.npc = npc;
    }
    public int getQuestID() {
        return questID;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getActive() {
        return active;
    }

    public void setItemAmount(int i){
        itemAmount = i;
    }

    public void setReward(double d){
        reward = d;
    }
}
