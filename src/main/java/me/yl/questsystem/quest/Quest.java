package me.yl.questsystem.quest;

import me.yl.questsystem.npc.NPC;
import org.bukkit.inventory.ItemStack;

public class Quest {

    private final ItemStack item;
    private final int itemAmount;
    private final double reward;
    private NPC npc;
    private final int questID;

    public Quest(ItemStack item, int itemAmount, double reward, NPC npc){
        this.item = item;
        this.itemAmount = itemAmount;
        this.reward = reward;
        this.npc = npc;
        questID = new QuestManager().getQuestList().get(npc).size()+1;
    }

    public Quest(ItemStack item, int itemAmount, double reward, NPC npc, int questID){
        this.item = item;
        this.itemAmount = itemAmount;
        this.reward = reward;
        this.npc = npc;
        this.questID = questID;
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
}
