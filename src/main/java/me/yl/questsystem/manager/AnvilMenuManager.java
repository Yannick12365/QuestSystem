package me.yl.questsystem.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class AnvilMenuManager implements Listener {
    public static final ArrayList<Inventory> invList = new ArrayList<>();

    public void createAnvilMenu(Player p, ItemStack[] items, String n) {
        Inventory inv = Bukkit.createInventory(p, InventoryType.ANVIL, n);
        inv.setContents(items);
        invList.add(inv);
        p.openInventory(inv);
    }

    public ArrayList<Inventory> getInvList(){
        return invList;
    }

    public void removeInv(Inventory inv){
        invList.remove(inv);
    }

    public Inventory checkForInv(Inventory inv){
        for (Inventory invFound:invList){
            if (invFound == inv){
                return invFound;
            }
        }
        return null;
    }

    @EventHandler
    public void clickEvent(InventoryClickEvent e){
        if (new AnvilMenuManager().checkForInv(e.getClickedInventory()) != null){
            e.setCancelled(true);
            if (e.getSlot() == 2) {
                new AnvilMenuManager().removeInv(e.getInventory());
            }
        }
    }

    @EventHandler
    public void closeEvent(InventoryCloseEvent e){
        if (new AnvilMenuManager().checkForInv(e.getInventory()) != null){
            new AnvilMenuManager().removeInv(e.getInventory());
        }
    }
}
