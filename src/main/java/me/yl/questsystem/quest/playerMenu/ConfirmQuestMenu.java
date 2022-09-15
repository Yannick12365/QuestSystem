package me.yl.questsystem.quest.playerMenu;

import me.oxolotel.utils.bukkit.menuManager.menus.Closeable;
import me.oxolotel.utils.bukkit.menuManager.menus.CustomMenu;
import me.oxolotel.utils.bukkit.menuManager.menus.SlotCondition;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryContent;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryItem;
import me.yl.questsystem.manager.ItemManager;
import me.yl.questsystem.npc.NPC;
import me.yl.questsystem.quest.Quest;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ConfirmQuestMenu extends CustomMenu implements Closeable, SlotCondition {

    private Quest quest;
    private NPC npc;
    private InventoryContent content;

    public ConfirmQuestMenu(int size, Quest quest) {
        super(size);
        this.quest = quest;
        this.npc = quest.getNpc();
        this.content = new InventoryContent();
        setTitle("Quest abgeben bestätigen");
    }

    @Override
    public void onClose(Player player, ItemStack[] itemStacks, CloseReason closeReason) {

    }

    @Override
    public InventoryContent getContents(Player player) {
        content.fill(0, 54, new InventoryItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), ()-> {}));
        content.addGuiItem(38, new InventoryItem(new ItemManager(Material.RED_BANNER).build(), ()->{}));
        content.addGuiItem(42, ()-> {}, Material.GREEN_BANNER);


        return content;
    }

    @Override
    public boolean isClickAllowed(Player player, int i) {
        return false;
    }
}
