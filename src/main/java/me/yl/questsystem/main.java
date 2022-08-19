package me.yl.questsystem;

import me.oxolotel.utils.bukkit.menuManager.InventoryMenuManager;
import me.oxolotel.utils.wrapped.module.ModuleManager;
import me.yl.questsystem.commands.Quest;
import me.yl.questsystem.listener.MenuClick;
import me.yl.questsystem.listener.PlayerJoin;
import me.yl.questsystem.npc.NPCConfigManager;
import me.yl.questsystem.quest.QuestConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {
    @Override
    public void onEnable() {
        getCommand("Quest").setExecutor(new Quest());
        ModuleManager.loadModule(ModuleManager.getPluginModuleByClass(InventoryMenuManager.class));
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new MenuClick(), this);

        new NPCConfigManager().createConfigConfiguration(this);
        new NPCConfigManager().readNPCConfig();

        new QuestConfigManager().createConfigConfiguration(this);
        new QuestConfigManager().readQuestConfig();
    }

    @Override
    public void onDisable() {
    }
}
