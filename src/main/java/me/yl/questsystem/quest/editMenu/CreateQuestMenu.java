package me.yl.questsystem.quest.editMenu;

import me.oxolotel.utils.bukkit.menuManager.menus.*;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryContent;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryItem;
import me.yl.questsystem.manager.ItemManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CreateQuestMenu extends CustomMenu implements Closeable, SlotCondition, Modifyable, Submenu, CommandModifyable {

    private boolean commandCheck;
    private InventoryContent content;
    private Player p;

    public CreateQuestMenu(int size) {
        super(size);
        setTitle("Create Quest");
        content = new InventoryContent();
    }

    @Override
    public void onClose(Player player, ItemStack[] itemStacks, CloseReason closeReason) {

    }

    @Override
    public InventoryContent getContents(Player player) {
        p= player;
      //  InventoryContent c = new InventoryContent();
        content.fill(0,54, new InventoryItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), ()->{}));
        content.addGuiItem(13, new InventoryItem(new ItemManager(Material.DIAMOND_PICKAXE).setDisplayName("Quest erstellen").build(), ()->{}));
        content.addGuiItem(28, new InventoryItem(new ItemManager(Material.OAK_SIGN).setDisplayName("Item Menge").build(), ()->{
            commandCheck = false;
            awaitCommand(player);}));
        content.addGuiItem(30, new InventoryItem(new ItemManager(Material.OAK_SIGN).setDisplayName("Item Preis").build(), ()->{
            commandCheck = true;
            awaitCommand(player);}));
        content.addGuiItem(37, null);
        content.addGuiItem(38, new InventoryItem(new ItemManager(Material.ARROW).build(), ()->{}));
        content.addGuiItem(39, new InventoryItem(new ItemManager(Material.GOLD_INGOT).setDisplayName("").build(), ()->{}));
        content.addGuiItem(41, new InventoryItem(new ItemManager(Material.RED_BANNER).setDisplayName("Abbrechen").build(), ()->{player.closeInventory();}));
        content.addGuiItem(43, new InventoryItem(new ItemManager(Material.GREEN_BANNER).setDisplayName("Speichern").build(), ()->{}));


        return content;
    }

    @Override
    public boolean isClickAllowed(Player player, int i) {
        if(i==37 || i == 41){
            return true;
        }
        return false;
    }


    @Override
    public String getCommand() {

        if (commandCheck == true){
            return "QuestPreis";
        }else if (commandCheck == false){
            return "QuestMenge";
        }
        return null;
    }

    @Override
    public String getCommandHelp() {
        if (commandCheck == true){
           return  "Gib /QuestPreis <Preis> ein um den Preis festzulegen";
        }else if (commandCheck == false){
            return  "Gib /QuestMenge <Menge> ein um die Menge festzulegen";
        }
        return null;
    }

    @Override
    public void processCommand(String[] strings) {

        if (commandCheck == true){

            content.addGuiItem(30, new InventoryItem(new ItemManager(Material.OAK_SIGN).setDisplayName("Item Preis").setLore(strings).build(), ()->{
                commandCheck = true;
                awaitCommand(p);}));

        }else if (commandCheck == false){

            content.addGuiItem(28, new InventoryItem(new ItemManager(Material.OAK_SIGN).setDisplayName("Item Menge").setLore(strings).build(), ()->{
                commandCheck = false;
                awaitCommand(p);}));
        }

    }
}
