package me.yl.questsystem.quest;

import me.oxolotel.utils.bukkit.menuManager.menus.Closeable;
import me.oxolotel.utils.bukkit.menuManager.menus.CustomMenu;
import me.oxolotel.utils.bukkit.menuManager.menus.Submenu;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryContent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EditSingleQuestMenu extends CustomMenu implements Closeable, Submenu {


    public EditSingleQuestMenu(int size, int questID) {
        super(size);
    }

    @Override
    public InventoryContent getContents(Player player) {
        return null;
    }

    @Override
    public void onClose(Player player, ItemStack[] itemStacks, CloseReason closeReason) {

    }
}
