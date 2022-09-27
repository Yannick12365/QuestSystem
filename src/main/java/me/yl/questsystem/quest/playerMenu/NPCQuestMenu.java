package me.yl.questsystem.quest.playerMenu;

import com.mysql.cj.x.protobuf.MysqlxCursor;
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

public class NPCQuestMenu extends CustomMenu implements Closeable, SlotCondition {

    private NPC npc;
    private InventoryContent content;
    private int questPacket;


    public NPCQuestMenu(int size, NPC npc, int nr) {
        super(size);
        this.npc = npc;
        this.content = new InventoryContent();
        questPacket = nr;
        setTitle("Questi Quest");
    }

    @Override
    public void onClose(Player player, ItemStack[] itemStacks, CloseReason closeReason) {

    }

    @Override
    public InventoryContent getContents(Player player) {
        content.fill(0,54, new InventoryItem(new ItemManager(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").build(), ()->{}));
        content.fill(11, 16, new InventoryItem(new ItemManager(Material.BOOK).build(), ()->{
            InventoryMenuManager.getInstance().closeMenu(player, CloseReason.CHANGEMENU);
            InventoryMenuManager.getInstance().openMenu(player, new ConfirmQuestMenu(54, new QuestManager().getNPCQuests(npc.getName(),questPacket).get(1)));

        }));
        content.fill(20, 25, new InventoryItem(new ItemManager(Material.RED_CONCRETE).build(), ()->{}));
        content.addGuiItem(37, new InventoryItem(new ItemManager(Material.GLOWSTONE_DUST).build(), ()->{}));
        content.addGuiItem(43, ()-> {}, Material.BARRIER);
        return content;
    }

    @Override
    public boolean isClickAllowed(Player player, int i) {
        return true;
    }
}
