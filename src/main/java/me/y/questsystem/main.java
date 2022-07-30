package me.y.questsystem;

import me.y.questsystem.commands.Quest;
import me.y.questsystem.listener.PlayerJoin;
import me.y.questsystem.listener.PlayerQuit;
import me.y.questsystem.manager.ClickPacketReader;
import me.y.questsystem.manager.ConfigManager;
import me.y.questsystem.manager.NPCManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {
    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("Quest").setExecutor(new Quest());
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuit(), this);

        new ConfigManager().createConfigConfiguration(this);
        new ConfigManager().readNPCconfig();

        if (Bukkit.getOnlinePlayers() != null) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                new NPCManager().sentJoinPacket(p);
                new ClickPacketReader(p).inject();
            }
        }
    }

    @Override
    public void onDisable() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            new ClickPacketReader(p).uninject();
        }
    }
}
