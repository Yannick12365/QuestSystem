package me.yl.questsystem;

import me.yl.questsystem.commands.Quest;
import me.yl.questsystem.listener.PlayerJoin;
import me.yl.questsystem.listener.PlayerQuit;
import me.yl.questsystem.npc.NPCConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {
    @Override
    public void onEnable() {
        getCommand("Quest").setExecutor(new Quest());
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuit(), this);

        new NPCConfigManager().createConfigConfiguration(this);
        new NPCConfigManager().readNPCConfig();
    }

    @Override
    public void onDisable() {
    }
}
