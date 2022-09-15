package me.yl.questsystem.listener;

import me.oxolotel.utils.bukkit.menuManager.events.implement.log.MenuLogModifyEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class MenuClick implements Listener {
    private static final HashMap<UUID, ItemStack> itemMap = new HashMap<>();

    @EventHandler
    public void menuModifyClickListener(MenuLogModifyEvent e){
        if (e.getChangedSlots().containsKey(37) && e.getMenu().getTitle().equals("Create Quest")){
            itemMap.put(e.getPlayer().getUniqueId(), new ItemStack(e.getChangedSlots().get(37).getValue2()));
        }
    }

    public ItemStack getItem(Player p){
        return itemMap.get(p.getUniqueId());
    }

    public void removePlayer(Player p){
        itemMap.remove(p.getUniqueId());
    }
}