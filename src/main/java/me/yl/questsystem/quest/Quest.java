package me.yl.questsystem.quest;

import me.yl.questsystem.npc.NPC;
import me.yl.questsystem.npc.NPCManager;
import me.yl.questsystem.quest.QuestManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Quest {

    private ItemStack item;
    private int itemAmount;
    private double reward;
    private NPC npc;
    private int questID;

    public Quest(ItemStack item, int itemAmount, double reward, NPC npc, QuestManager qm){
        this.item = item;
        this.itemAmount = itemAmount;
        this.reward = reward;
        this.npc = npc;
        questID = qm.getQuestList().get(npc).size()+1;
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
    public void setItem(ItemStack item) {
        this.item = item;
    }
    public int getItemAmount() {
        return itemAmount;
    }
    public void setItemAmount(int itemAmount) {
        this.itemAmount = itemAmount;
    }
    public double getReward() {
        return reward;
    }
    public void setReward(double reward) {
        this.reward = reward;
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
    public void setQuestID(int questID) {
        this.questID = questID;
    }

}
