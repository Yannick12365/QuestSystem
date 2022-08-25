package me.yl.questsystem.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class AnvilMenuManager implements Listener {
    public static final ArrayList<Inventory> invList = new ArrayList<>();
    public static HashMap<UUID,ItemStack> itemList = new HashMap<>();

    public void createanvilMenu(Player p, ItemStack[] items, String n){
        Inventory inv = Bukkit.createInventory(null, InventoryType.ANVIL, n);
        inv.setContents(items);

        invList.add(inv);
        p.openInventory(inv);
    }

    public HashMap<UUID,ItemStack> getItemList(){
        return itemList;
    }

   public void removeItem(Player p){
        itemList.remove(p.getUniqueId());
   }

    @EventHandler
    public void clickAnvilMenuListener(InventoryClickEvent e){
        for (Inventory inv:invList){
            if (inv != e.getClickedInventory()){
                return;
            }
        }
        //e.setCancelled(true);

        if (e.getSlot() != 2){
            return;
        }
        invList.remove(e.getClickedInventory());
        itemList.put(e.getWhoClicked().getUniqueId(),e.getClickedInventory().getItem(2));
        e.getWhoClicked().closeInventory();
    }
}
