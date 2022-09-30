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
import me.yl.questsystem.manager.SettingsConfigManager;
import me.yl.questsystem.npc.NPCConfigManager;
import me.yl.questsystem.quest.QuestConfigManager;

import me.yl.questsystem.quest.QuestManager;
import me.yl.questsystem.quest.WeeklyQuestConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDate;

public final class main extends JavaPlugin {

    public static final String PREFIX = "§7§l[§b§lQuest§7§l]";

    @Override
    public void onEnable() {
        getCommand("Quest").setExecutor(new QuestCommand());

        ModuleManager.loadModule(ModuleManager.getPluginModuleByClass(InventoryMenuManager.class));

        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new MenuClick(), this);
        Bukkit.getPluginManager().registerEvents(new AnvilMenuManager(),this);

        WeeklyQuestConfigManager weeklyQuestConfigManager = new WeeklyQuestConfigManager();
        weeklyQuestConfigManager.createConfigConfiguration(this);

        NPCConfigManager npcConfigManager = new NPCConfigManager();
        npcConfigManager.createConfigConfiguration(this);

        SettingsConfigManager settingsConfigManager = new SettingsConfigManager();
        settingsConfigManager.createConfigConfiguration(this);

        QuestConfigManager questConfigManager = new QuestConfigManager();
        questConfigManager.createConfigConfiguration(this);
        settingsConfigManager.writeQuestPacketNumber(0);
        npcConfigManager.readNPCConfig();
        questConfigManager.readQuestConfig(settingsConfigManager.readQuestPacketNumber());

        if (LocalDate.now().getDayOfWeek().getValue() == 5){
            weeklyQuestConfigManager.clearWeeklyQuesttConfig();
            settingsConfigManager.writeQuestPacketNumber(settingsConfigManager.readQuestPacketNumber());
            weeklyQuestConfigManager.writeWeeklyQuesttConfig();
        }
        weeklyQuestConfigManager.readWeeklyQuestConfig();

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        ProtocolLibReader protocolLibReader = new ProtocolLibReader();
        protocolLibReader.readNPCClickPacket(protocolManager, this);
        protocolLibReader.readWindowClickPacket(protocolManager, this);
    }

    @Override
    public void onDisable() {
    }
}
