package me.yl.questsystem.listener;

import me.oxolotel.utils.bukkit.menuManager.events.implement.MenuClickEvent;
import me.oxolotel.utils.bukkit.menuManager.events.implement.log.MenuLogModifyEvent;
import me.yl.questsystem.manager.ItemManager;
import me.yl.questsystem.quest.editMenu.CreateQuestMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class MenuClick implements Listener {
    private static final HashMap<UUID, ItemStack> itemMap = new HashMap<>();
    private static HashMap<UUID, Boolean> checkItem = new HashMap<>();


    @EventHandler
    public void menuModifyClickListener(MenuLogModifyEvent e){
        e.getPlayer().sendMessage("Test");
        if (e.getChangedSlots().containsKey(37) && e.getMenu().getTitle().equals("Create Quest")){
            itemMap.put(e.getPlayer().getUniqueId(), new ItemStack(e.getChangedSlots().get(37).getValue2()));
        } else if (e.getChangedSlots().containsKey(43) && e.getMenu().getTitle().equals("Create Quest")) {
            if (e.getChangedSlots().get(43).getValue1() == null || e.getChangedSlots().get(43).getValue1().getType() == Material.AIR){
                if (e.getChangedSlots().get(43).getValue2() != null || e.getChangedSlots().get(43).getValue2().getType() != Material.AIR){
                    checkItem.put(e.getPlayer().getUniqueId(),true);
                }
            } else if (e.getChangedSlots().get(43).getValue2() != null || e.getChangedSlots().get(43).getValue2().getType() != Material.AIR &&
                e.getChangedSlots().get(43).getValue1() != null || e.getChangedSlots().get(43).getValue1().getType() != Material.AIR){
                checkItem.put(e.getPlayer().getUniqueId(),true);
            } else {
                checkItem.put(e.getPlayer().getUniqueId(),false);
            }
        }
    }


    public ItemStack getItem(Player p){
        return itemMap.get(p.getUniqueId());
    }

    public void removePlayer(Player p){
        itemMap.remove(p.getUniqueId());
    }

    public static HashMap<UUID, ItemStack> getItemMap() {
        return itemMap;
    }

    public void removeItemPlayer(Player p){
        checkItem.remove(p.getUniqueId());
    }
    public static HashMap<UUID, Boolean> getCheckItem(){
        return checkItem;
    }

    public void setCheckItem(boolean check, Player p){
        checkItem.put(p.getUniqueId(), check);
    }
}