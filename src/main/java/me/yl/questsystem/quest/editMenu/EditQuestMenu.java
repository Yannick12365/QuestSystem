package me.yl.questsystem.quest.editMenu;

import me.oxolotel.utils.bukkit.menuManager.menus.*;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryContent;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryItem;
import me.yl.questsystem.quest.Quest;
import me.yl.questsystem.quest.QuestManager;
import me.yl.questsystem.quest.editMenu.EditSingleQuestMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class EditQuestMenu extends CustomMenu implements Closeable, Subdevideable, Pageable {


    private static HashMap<Integer, Integer> slotQuestID;
    private static String NpcName;
    private static Player p;
    private static int GuiSize;

    public EditQuestMenu(int size, String name) {
        super(size);
        GuiSize = size;
        slotQuestID = new HashMap<>();
        NpcName = name;
        setTitle("Edit Quest");
    }

    @Override
    public InventoryContent getContents(Player player) {
        InventoryContent c = new InventoryContent();
        c.fill(0,GuiSize, new InventoryItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), ()->{}));
        int slots = 0;
        int questIDCount = 1;
        int questCount = new QuestManager().countNPCQuests(NpcName);
        if (questCount != 0) {
            for (int i = questCount; i > 0; i -= 21) {
                c = fillQuests(c, i, slots, questIDCount);
                slots += 54;
                questIDCount += 21;
            }
        }

       return c;
    }

    @Override
    public void onClose(Player player, ItemStack[] itemStacks, CloseReason closeReason) {

    }

    private InventoryContent fillQuests(InventoryContent c, int cq, int cs, int qid){
        int counterInv = 0;
        int counterQuests = cq;
        int counterSlot = cs+8;
        int questid = qid;

        if (counterQuests == 0) return c;
        for (int i = 0; i < 3; i++){
            counterSlot +=2;
            for (int z = 0; z < 7; z++){
                ItemStack questItem = new ItemStack(Material.BOOK);
                ItemMeta questMeta = questItem.getItemMeta();
                Quest quest = new QuestManager().getNPCQuestByID(NpcName, questid);

                slotQuestID.put(counterSlot,1);
                c.addGuiItem(counterSlot, new InventoryItem(questItem, ()-> {}) );

                counterSlot++;
                counterInv++;
                questid++;
                if(counterInv == 21 || counterInv == counterQuests) return c;

            }
        }
        return c;
    }

    @Override
    public boolean hasSubmenu(int i) {
        if(!slotQuestID.containsKey(i))return false;
        return true;
    }

    @Override
    public CustomMenu getSubmenu(int i) {
        p.sendMessage(Integer.toString(i));
        if(!slotQuestID.containsKey(i))return null;
        return new EditSingleQuestMenu(54, slotQuestID.get(i));
    }
}
