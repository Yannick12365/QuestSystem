package me.yl.questsystem.quest.editMenu;

import me.oxolotel.utils.bukkit.menuManager.InventoryMenuManager;
import me.oxolotel.utils.bukkit.menuManager.menus.*;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryContent;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryItem;
import me.yl.questsystem.manager.ItemManager;
import me.yl.questsystem.npc.NPC;
import me.yl.questsystem.quest.Quest;
import me.yl.questsystem.quest.QuestManager;
import me.yl.questsystem.quest.editMenu.EditSingleQuestMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditQuestMenu extends CustomMenu implements Closeable, Subdevideable, Pageable, Submenu {


    private static HashMap<Integer, Integer> slotQuestID;
    private static NPC npc;
    private static int guiSize;
    private ArrayList<Integer> createSlotList;

    public EditQuestMenu(int size, NPC npc) {
        super(size);
        guiSize = size;
        slotQuestID = new HashMap<>();
        this.npc = npc;
        setTitle("Edit Quest");
        createSlotList = new ArrayList<>();
    }

    @Override
    public InventoryContent getContents(Player player) {
        InventoryContent c = new InventoryContent();
        c.fill(0,guiSize, new InventoryItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), ()->{}));
        int slots = 0;
        int questIDCount = 0;
        int questCount = new QuestManager().countNPCQuests(npc.getName());
        ArrayList<Quest> questLists = new QuestManager().getNPCQuests(npc.getName());

        ItemStack erstellItem = new ItemManager(Material.DIAMOND_PICKAXE).setDisplayName("Quest erstellen").build();
        ItemStack closeItem = new ItemManager(Material.BARRIER).setDisplayName("Menü verlassen").build();

        createSlotList.add(40);
        c.addGuiItem(40, new InventoryItem(erstellItem, () -> {}));
        //InventoryMenuManager.getInstance().openMenu(player, new CreateQuestMenu(54));

        c.addGuiItem(42, new InventoryItem(closeItem, () -> {InventoryMenuManager.getInstance().closeMenu(player);}));
            player.sendMessage(String.valueOf(questLists.size()));
        if (!(questCount == 0 || questLists.size() == 0)) {

            for (int i = questCount; i > 0; i -= 21) {
                c = fillQuests(c, i, slots, questLists, questIDCount);
                slots += 45;
                questIDCount += 21;
                createSlotList.add(slots-14 +9);
                c.addGuiItem(slots - 14 + 9, new InventoryItem(erstellItem, () -> {}));
                //InventoryMenuManager.getInstance().openMenu(player, new CreateQuestMenu(54));
                c.addGuiItem(slots - 12 + 9, new InventoryItem(closeItem, () -> {InventoryMenuManager.getInstance().closeMenu(player);}));
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

                ArrayList<String> lore = new ArrayList<>();
                lore.add("Gib "+quest.getItemAmount()+" "+quest.getItem().toString()+ "ab und erhalte "+quest.getReward());
                ItemStack questItem = new ItemManager(Material.BOOK).setDisplayName("Quest").setLore(lore).build();

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
        return slotQuestID.containsKey(i) || createSlotList.contains(i);
    }

    @Override
    public CustomMenu getSubmenu(int i) {
        if(createSlotList.contains(i)){
            return new CreateQuestMenu(54, npc);
        }
        if(!slotQuestID.containsKey(i)) {
            return null;
        }
        return new EditSingleQuestMenu(54, slotQuestID.get(i));
    }
}
