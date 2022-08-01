package me.yl.questsystem.quest;

import me.oxolotel.utils.bukkit.menuManager.menus.Closeable;
import me.oxolotel.utils.bukkit.menuManager.menus.CustomMenu;
import me.oxolotel.utils.bukkit.menuManager.menus.Modifyable;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryContent;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Menu extends CustomMenu implements Closeable, Modifyable {


    public Menu(int size) {
        super(size);

    }

    @Override
    public InventoryContent getContents(Player player) {

        return new InventoryContent().addGuiItem(5, ()-> player.sendMessage("test"), Material.GLOW_ITEM_FRAME);
    }

    @Override
    public void onClose(Player player, ItemStack[] itemStacks, CloseReason closeReason) {

    }
}
