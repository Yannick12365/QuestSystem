package me.yl.questsystem.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener{
    @EventHandler
    public void quitEvent(PlayerQuitEvent event){
        //new PacketReader(event.getPlayer()).uninject();
    }
}
