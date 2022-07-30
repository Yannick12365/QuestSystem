package me.yl.questsystem.listener;

import me.yl.questsystem.npc.ClickPacketReader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerQuit implements Listener{
    @EventHandler
    public void quitEvent(PlayerJoinEvent event){
        new ClickPacketReader(event.getPlayer()).uninject();
    }
}
