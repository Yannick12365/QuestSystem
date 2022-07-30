package me.yl.questsystem.listener;

import me.yl.questsystem.npc.ClickPacketReader;
import me.yl.questsystem.npc.NPCManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener{
    @EventHandler
    public void joinEvent(PlayerJoinEvent event){
        new NPCManager().sentJoinPacket(event.getPlayer());
        new ClickPacketReader(event.getPlayer()).inject();
    }
}
