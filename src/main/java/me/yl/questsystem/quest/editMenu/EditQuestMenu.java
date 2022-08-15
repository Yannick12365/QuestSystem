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

import java.util.ArrayList;
import java.util.HashMap;

public class EditQuestMenu extends CustomMenu implements Closeable, Subdevideable, Pageable {


    private static HashMap<Integer, Integer> slotQuestID;
    private static String NpcName;
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
        ArrayList<Quest> questLists = new QuestManager().getNPCQuests(NpcName);

        ItemStack erstellItem = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta erstellMeta = erstellItem.getItemMeta();
        erstellMeta.setDisplayName("Quest erstellen");
        erstellItem.setItemMeta(erstellMeta);

        ItemStack closeItem = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeItem.getItemMeta();
        closeMeta.setDisplayName("Menü verlassen");
        closeItem.setItemMeta(closeMeta);

        c.addGuiItem(40, new InventoryItem(erstellItem, () -> {}));
        c.addGuiItem(42, new InventoryItem(closeItem, () -> {}));

        if (questCount != 0 || questLists == null) {
            for (int i = questCount; i > 0; i -= 21) {
                c = fillQuests(c, i, slots, questLists, questIDCount);
                slots += 45;
                questIDCount += 21;

                c.addGuiItem(slots - 14 + 9, new InventoryItem(erstellItem, () -> {}));
                c.addGuiItem(slots - 12 + 9, new InventoryItem(closeItem, () -> {}));
            }
        }
       return c;
    }

    @Override
    public void onClose(Player player, ItemStack[] itemStacks, CloseReason closeReason) {

    }

    private InventoryContent fillQuests(InventoryContent c, int cq, int cs, ArrayList<Quest> qls, int qid){
        int counterInv = 0;
        int counterQuests = cq;
        int counterSlot = cs+8;
        int questIDCount = qid;
        ArrayList<Quest> questList = qls;

        if (counterQuests == 0) return c;
        for (int i = 0; i < 3; i++){
            counterSlot +=2;
            for (int z = 0; z < 7; z++){
                Quest quest = questList.get(questIDCount);
                ItemStack questItem = new ItemStack(Material.BOOK);
                ItemMeta questMeta = questItem.getItemMeta();
                questMeta.setDisplayName("Quest");
                ArrayList<String> lore = new ArrayList<>();
                lore.add("Gib "+quest.getItemAmount()+" "+quest.getItem().toString()+ "ab und erhalte "+quest.getReward());
                questMeta.setLore(lore);
                questItem.setItemMeta(questMeta);
                slotQuestID.put(counterSlot,1);
                c.addGuiItem(counterSlot, new InventoryItem(questItem, ()-> {}) );

                counterSlot++;
                counterInv++;
                questIDCount++;
                if(counterInv == 21 || counterInv == counterQuests) {
                    return c;
                }
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
        if(!slotQuestID.containsKey(i))return null;
        return new EditSingleQuestMenu(54, slotQuestID.get(i));
    }
}
