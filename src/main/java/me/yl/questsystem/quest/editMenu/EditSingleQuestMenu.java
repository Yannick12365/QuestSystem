package me.yl.questsystem.quest.editMenu;

import me.oxolotel.utils.bukkit.menuManager.InventoryMenuManager;
import me.oxolotel.utils.bukkit.menuManager.menus.*;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryContent;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryItem;
import me.yl.questsystem.manager.ItemManager;
import me.yl.questsystem.npc.NPC;
import me.yl.questsystem.quest.Quest;
import me.yl.questsystem.quest.QuestManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class EditSingleQuestMenu extends CustomMenu implements Closeable, SlotCondition, Subdevideable {

    private int questID;
    private String n;
    private InventoryContent content;
    private NPC npc;

    public EditSingleQuestMenu(int size, int questID, NPC npc) {
        super(size);
        this.questID = questID;
        this.n = npc.getName();
        setTitle("Quest verwalten");
        content = new InventoryContent();
        this.npc = npc;
    }

    @Override
    public InventoryContent getContents(Player player) {

        content.fill(0, 54, new InventoryItem(new ItemManager(Material.GRAY_STAINED_GLASS_PANE).build(), ()-> {}));
        if (new QuestManager().getNPCQuestByID(n, questID).getActive()) {
            content.addGuiItem(37, new InventoryItem(new ItemManager(Material.REDSTONE_TORCH).setDisplayName("§c§lAktiv").build(), () -> {changeActive(player);}));
        }else {
            content.addGuiItem(37, new InventoryItem(new ItemManager(Material.LEVER).setDisplayName("§7§lInaktiv").build(), () -> {changeActive(player);}));
        }

        Quest quest = new QuestManager().getNPCQuestByID(npc.getName(), questID);
        ArrayList<String> lore = new ArrayList<>();
        lore.add("Gib "+quest.getItemAmount()+" "+quest.getItem().toString()+ "ab und erhalte "+quest.getReward());
        ItemStack questItem = new ItemManager(Material.BOOK).setDisplayName("Quest").setLore(lore).build();

        content.addGuiItem(13, new InventoryItem(questItem, ()->{})); //TODO Formatieren
        content.addGuiItem(39, ()->{},Material.BLUE_DYE, "§9§lBearbeiten");
        content.addGuiItem(41, ()->{},Material.RED_DYE, "§4§lLöschen");
        content.addGuiItem(43, ()->{
            InventoryMenuManager.getInstance().openMenu(player, new EditQuestMenu(new QuestManager().getEditQuestGUISize(npc), npc));},
                Material.BARRIER, "§c§lZurück");
        return content;
    }

    @Override
    public void onClose(Player player, ItemStack[] itemStacks, CloseReason closeReason) {

    }

    private void changeActive(Player p){
        new QuestManager().changeActiveStatus(!content.get(37).getItem().getType().equals(Material.REDSTONE_TORCH), n,questID);
        InventoryMenuManager.getInstance().getOpenMenu(p).refresh();
    }

    @Override
    public boolean isClickAllowed(Player p, int slot) {
        return (slot == 37 || slot == 39 || slot == 41 || slot == 43);
    }

    @Override
    public boolean hasSubmenu(int slot) {

        return (slot == 41 || slot == 39);
    }

    @Override
    public CustomMenu getSubmenu(int slot) {

        if(slot == 41){
            return new DeleteQuestMenu(54, new QuestManager().getNPCQuestByID(n, questID), npc);
        } else if (slot == 39) {
            return new ReeditQuestMenu(54, npc, new QuestManager().getNPCQuestByID(npc.getName(),questID));
        }
        return null;
    }
}
