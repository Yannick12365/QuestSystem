package me.yl.questsystem.npc;

import me.oxolotel.utils.bukkit.menuManager.InventoryMenuManager;
import me.oxolotel.utils.bukkit.menuManager.menus.Closeable;
import me.oxolotel.utils.bukkit.menuManager.menus.CustomMenu;
import me.oxolotel.utils.bukkit.menuManager.menus.SlotCondition;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryContent;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryItem;
import me.yl.questsystem.manager.ItemManager;
import me.yl.questsystem.manager.SettingsConfigManager;
import me.yl.questsystem.quest.QuestManager;
import me.yl.questsystem.quest.editMenu.SelectQuestPacketMenu;
import me.yl.questsystem.quest.playerMenu.NPCQuestMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NPCMenu extends CustomMenu implements Closeable, SlotCondition {

    private NPC npc;
    InventoryContent content;

    public NPCMenu(int size, NPC npc) {
        super(size);
        this.npc = npc;
        content = new InventoryContent();
    }

    @Override
    public void onClose(Player player, ItemStack[] itemStacks, CloseReason closeReason) {

    }

    @Override
    public InventoryContent getContents(Player player) {
        content.fill(0,54, new InventoryItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), ()->{}));
        content.addGuiItem(11, new InventoryItem(new ItemManager(Material.GLOWSTONE_DUST).setDisplayName("Anmerkungen").build(),()->{

        }));
        if (npc.isWochenBonus()) {
            content.addGuiItem(13, new InventoryItem(new ItemManager(Material.SOUL_TORCH).setDisplayName("Wochenbonus").build(), () -> {
                changeWochenbonus(false, player);
            }));
        }else {
            content.addGuiItem(13, new InventoryItem(new ItemManager(Material.LEVER).setDisplayName("Wochenbonus").build(), () -> {
                changeWochenbonus(true, player);
            }));
        }
        if (npc.isStatus()) {
            content.addGuiItem(15, new InventoryItem(new ItemManager(Material.REDSTONE_TORCH).setDisplayName("NPC Status").build(), () -> {
                changeStatus(false, player);
            }));
        }else {
            content.addGuiItem(15, new InventoryItem(new ItemManager(Material.LEVER).setDisplayName("NPC Status").build(), () -> {
                changeStatus(true, player);
            }));
        }
        content.addGuiItem(31,new InventoryItem(new ItemManager(Material.LEGACY_BOOK_AND_QUILL).setDisplayName("Quest Menü").build(),()->{
            InventoryMenuManager.getInstance().openMenu(player, new SelectQuestPacketMenu(54, npc));
        }));
        content.addGuiItem(43,new InventoryItem(new ItemManager(Material.BARRIER).setDisplayName("Schließen").build(),()->{
            InventoryMenuManager.getInstance().closeMenu(player, CloseReason.OTHER);
        }));

        return content;
    }

    @Override
    public boolean isClickAllowed(Player player, int i) {
        return i == 11 || i == 13 || i == 15 || i == 31 || i == 43;
    }

    private void changeStatus(boolean status, Player p){
        new NPCManager().changeNPCStatus(npc, status);
        InventoryMenuManager.getInstance().getOpenMenu(p).refresh();
    }

    private void changeWochenbonus(boolean status, Player p){
        new NPCManager().changeNPCWochenbonus(npc, status);
        InventoryMenuManager.getInstance().getOpenMenu(p).refresh();
    }
}
