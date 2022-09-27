package me.yl.questsystem.quest.editMenu;

import me.oxolotel.utils.bukkit.menuManager.InventoryMenuManager;
import me.oxolotel.utils.bukkit.menuManager.menus.Closeable;
import me.oxolotel.utils.bukkit.menuManager.menus.CustomMenu;
import me.oxolotel.utils.bukkit.menuManager.menus.SlotCondition;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryContent;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryItem;
import me.yl.questsystem.manager.ItemManager;
import me.yl.questsystem.npc.NPC;
import me.yl.questsystem.quest.QuestManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SelectQuestPacketMenu extends CustomMenu implements Closeable, SlotCondition {

    private NPC npc;

    public SelectQuestPacketMenu(int size, NPC npc) {
        super(size);
        this.npc = npc;
    }

    @Override
    public void onClose(Player player, ItemStack[] itemStacks, CloseReason closeReason) {

    }

    @Override
    public InventoryContent getContents(Player player) {
        InventoryContent c = new InventoryContent();
        c.fill(0,53,new InventoryItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), ()->{}));

        c.addGuiItem(20,new InventoryItem(new ItemManager(Material.OAK_SAPLING).setDisplayName("Packet 1").build(),()->{
            int countSlots = new QuestManager().getEditQuestGUISize(npc, 0);
            InventoryMenuManager.getInstance().openMenu(player, new EditQuestMenu(countSlots, npc, 0));
        }));
        c.addGuiItem(22,new InventoryItem(new ItemManager(Material.BIRCH_SAPLING).setDisplayName("Packet 2").build(),()->{
            int countSlots = new QuestManager().getEditQuestGUISize(npc, 1);
            InventoryMenuManager.getInstance().openMenu(player, new EditQuestMenu(countSlots, npc, 1));
        }));
        c.addGuiItem(24,new InventoryItem(new ItemManager(Material.SPRUCE_SAPLING).setDisplayName("Packet 3").build(),()->{
            int countSlots = new QuestManager().getEditQuestGUISize(npc, 2);
            InventoryMenuManager.getInstance().openMenu(player, new EditQuestMenu(countSlots, npc, 2));
        }));
        c.addGuiItem(53,new InventoryItem(new ItemManager(Material.BARRIER).setDisplayName("Schließen").build(),()->{
            InventoryMenuManager.getInstance().closeMenu(player);
        }));

        return c;
    }

    @Override
    public boolean isClickAllowed(Player player, int i) {
        return i == 20 || i == 22 || i == 24;
    }
}
