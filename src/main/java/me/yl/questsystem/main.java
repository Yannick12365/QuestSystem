package me.yl.questsystem;

import me.yl.questsystem.commands.QuestCommand;
import me.yl.questsystem.listener.PlayerJoin;
import me.yl.questsystem.listener.PlayerQuit;
import me.yl.questsystem.manager.ClickPacketReader;
import me.yl.questsystem.manager.ConfigManager;
import me.yl.questsystem.manager.NPCManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {
    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("Quest").setExecutor(new QuestCommand());
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
