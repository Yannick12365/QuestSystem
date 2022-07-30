package me.yl.questsystem.quest;

import me.yl.questsystem.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Quest {

    private ItemStack item;
    private int itemAmount;
    private double reward;
    private NPC npc;
    private int questID;
    private Player creator;

    public Quest(ItemStack item, int itemAmount, double reward, NPC npc, Player creator, QuestManager qm){

        this.item = item;
        this.itemAmount = itemAmount;
        this.reward = reward;
        this.npc = npc;
        this.creator = creator;
        questID = qm.getQuestList().get(npc).size()+1;

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

    public Player getCreator() {
        return creator;
    }

    public void setCreator(Player creator) {
        this.creator = creator;
    }
}
