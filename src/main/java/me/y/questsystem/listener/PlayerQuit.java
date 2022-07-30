package me.y.questsystem.listener;

import me.y.questsystem.manager.ClickPacketReader;
import me.y.questsystem.manager.NPCManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerQuit implements Listener{
    @EventHandler
    public void quitEvent(PlayerJoinEvent event){
        new ClickPacketReader(event.getPlayer()).uninject();
    }
}
