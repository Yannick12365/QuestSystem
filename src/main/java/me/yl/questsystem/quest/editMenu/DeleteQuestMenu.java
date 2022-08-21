package me.yl.questsystem.quest.editMenu;

import me.oxolotel.utils.bukkit.menuManager.InventoryMenuManager;
import me.oxolotel.utils.bukkit.menuManager.menus.Closeable;
import me.oxolotel.utils.bukkit.menuManager.menus.CustomMenu;
import me.oxolotel.utils.bukkit.menuManager.menus.SlotCondition;
import me.oxolotel.utils.bukkit.menuManager.menus.Submenu;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryContent;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryItem;
import me.oxolotel.utils.wrapped.Chat;
import me.yl.questsystem.main;
import me.yl.questsystem.manager.ItemManager;
import me.yl.questsystem.npc.NPC;
import me.yl.questsystem.quest.Quest;
import me.yl.questsystem.quest.QuestManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class DeleteQuestMenu extends CustomMenu implements SlotCondition, Closeable, Submenu {


    private Quest q;
    private NPC npc;

    public DeleteQuestMenu(int size, Quest q, NPC npc) {
        super(size);
        setTitle("Löschen bestätigen");
        this.npc = npc;
        this.q = q;
    }

    @Override
    public void onClose(Player p, ItemStack[] content, CloseReason reason) {

    }

    @Override
    public InventoryContent getContents(Player p) {
        InventoryContent c = new InventoryContent();
        c.fill(0, 54, new InventoryItem(new ItemManager(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("").build(), ()->{}));
        c.addGuiItem(13, ()->{}, Material.RED_DYE, "§4§lQuest Löschen");
        c.addGuiItem(38, ()->{
            InventoryMenuManager.getInstance().closeMenu(p, CloseReason.RETRUNPREVIOUS);
            InventoryMenuManager.getInstance().getOpenMenu(p).refresh();
        },Material.RED_BANNER, "§c§lAbbrechen");
        c.addGuiItem(42, ()->{
            new QuestManager().removeQuest(q, npc);
            Chat.sendSuccessMessage(main.PREFIX, me.oxolotel.utils.wrapped.player.Player.of(p),"§c§lQuest erfolgreich gelöscht!");
            InventoryMenuManager.getInstance().closeMenu(p);
            InventoryMenuManager.getInstance().openMenu(p, new EditQuestMenu(new QuestManager().getEditQuestGUISize(npc), npc));
        } ,Material.GREEN_BANNER, "§a§lQuest löschen Bestätigen");

        return c;
    }

    @Override
    public boolean isClickAllowed(Player p, int slot) {
        return (slot == 38 || slot == 42);
    }
}
