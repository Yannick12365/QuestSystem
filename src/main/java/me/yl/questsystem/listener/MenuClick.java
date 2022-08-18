package me.yl.questsystem.listener;

import me.oxolotel.utils.bukkit.menuManager.events.implement.log.MenuLogClickEvent;
import me.oxolotel.utils.bukkit.menuManager.events.implement.log.MenuLogModifyEvent;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryContent;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.UUID;

public class MenuClick implements Listener {
    private static HashMap<UUID,InventoryContent> contentMap = new HashMap<>();

    public MenuClick(InventoryContent c, Player p){
        contentMap.put(p.getUniqueId(),c);
    }

    public MenuClick(){
    }

    @EventHandler
    public void createQuestMenuClickListener(MenuLogModifyEvent e){
        if (e.getChangedSlots().containsKey(37) && e.getMenu().getTitle() == "Create Quest"){
            contentMap.get(e.getPlayer().getUniqueId()).addGuiItem(37, new InventoryItem(e.getChangedSlots().get(37).getValue2(), ()->{}));
            e.getPlayer().sendMessage(contentMap.get(e.getPlayer().getUniqueId()).get(37).getItem().toString());
        }
    }

    public InventoryContent getContent(Player p){
        if (contentMap.get(p.getUniqueId()).get(37) != null){
        p.sendMessage(contentMap.get(p.getUniqueId()).get(37).getItem().toString()+ "--------------------");}
        return contentMap.get(p.getUniqueId());
    }
}
