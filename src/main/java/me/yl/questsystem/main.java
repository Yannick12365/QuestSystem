package me.yl.questsystem;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.oxolotel.utils.bukkit.menuManager.InventoryMenuManager;
import me.oxolotel.utils.wrapped.module.ModuleManager;

import me.yl.questsystem.commands.QuestCommand;
import me.yl.questsystem.listener.MenuClick;
import me.yl.questsystem.listener.PlayerJoin;
import me.yl.questsystem.manager.AnvilMenuManager;
import me.yl.questsystem.manager.ProtocolLibReader;
import me.yl.questsystem.npc.NPCConfigManager;
import me.yl.questsystem.quest.QuestConfigManager;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {

    public static final String PREFIX = "§7§l[§b§lQuest§7§l]";

    @Override
    public void onEnable() {
        getCommand("Quest").setExecutor(new QuestCommand());

        ModuleManager.loadModule(ModuleManager.getPluginModuleByClass(InventoryMenuManager.class));

        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new MenuClick(), this);
        Bukkit.getPluginManager().registerEvents(new AnvilMenuManager(),this);

        new NPCConfigManager().createConfigConfiguration(this);
        new NPCConfigManager().readNPCConfig();

        new QuestConfigManager().createConfigConfiguration(this);
        new QuestConfigManager().readQuestConfig();

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        new ProtocolLibReader().readNPCClickPacket(protocolManager, this);
        new ProtocolLibReader().readWindowClickPacket(protocolManager, this);
    }

    @Override
    public void onDisable() {
    }
}
