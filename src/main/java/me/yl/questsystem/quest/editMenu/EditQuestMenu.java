package me.yl.questsystem.quest.editMenu;

import me.oxolotel.utils.bukkit.menuManager.menus.*;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryContent;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryItem;
import me.yl.questsystem.quest.editMenu.EditSingleQuestMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class EditQuestMenu extends CustomMenu implements Closeable, Subdevideable, Pageable {


    private HashMap<Integer, Integer> subMenuID;
    private String NpcName;

    public EditQuestMenu(int size, String name) {
        super(size);
        subMenuID = new HashMap<>();
        NpcName = name;
        setTitle("Edit Quest");
    }

    @Override
    public InventoryContent getContents(Player player) {
        InventoryContent c = new InventoryContent();

        c.fillRow(0, new InventoryItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), () -> {}));
        c = fillQuests(c);
       return c;
    }

    @Override
    public void onClose(Player player, ItemStack[] itemStacks, CloseReason closeReason) {

    }

    private InventoryContent fillQuests(InventoryContent c){
        int counterInv = 0;
        int counterQuests = 32;
        int counterSlot = 8;
        if (counterQuests == 0) return c;
        for (int i = 0; i < 3; i++){
            counterSlot +=2;
            for (int z = 0; z < 7; z++){
                ItemStack questItem = new ItemStack(Material.BOOK);
                ItemMeta questMeta = questItem.getItemMeta();


                c.addGuiItem(counterSlot, new InventoryItem(questItem, ()-> {}) );

                counterSlot++;
                counterInv++;
                if(counterInv == 21 || counterInv == counterQuests) return c;

            }
        }
        return c;
    }

    @Override
    public boolean hasSubmenu(int i) {
        return false;
    }

    @Override
    public CustomMenu getSubmenu(int i) {
            if(!subMenuID.containsKey(i))return null;
            return new EditSingleQuestMenu(54, subMenuID.get(i));

    }
}
