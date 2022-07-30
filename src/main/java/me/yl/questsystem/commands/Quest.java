package me.yl.questsystem.commands;

import me.yl.questsystem.manager.ChatManager;

import me.yl.questsystem.npc.NPCManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Quest implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            return true;
        }
        Player player = (Player)sender;
        NPCManager npc = new NPCManager();

        if (args.length > 0){
            if (args[0].equalsIgnoreCase("help") && args.length == 1){
                helpText(player);
            } else if (args[0].equalsIgnoreCase("npc") && args.length > 1) {
                if (args[1].equalsIgnoreCase("create") && args.length == 3) {
                    npc.createNPC(player, args[2]);
                } else if (args[1].equalsIgnoreCase("remove") && args.length == 3) {
                    npc.removeNPC(player, args[2]);
                }else if (args[1].equalsIgnoreCase("tphere") && args.length == 3) {
                    npc.tpNPC(player, args[2]);
                }else if (args[1].equalsIgnoreCase("skin") && args.length == 4) {
                    npc.changeNPCSkin(player, args[2], args[3]);
                }
            }
        }else {
            helpText(player);
        }

        return false;
    }

    public void helpText(Player player){
        player.sendMessage("Wie funktioniert das Questsystem?");
        player.sendMessage("Bei unserem Questsystem kannst du deine Items bei Händlern am ganzen Spawn für Geld eintauschen. " +
                "Dabei kann jeder Händler nur eine bestimmte Anzahl an Items pro Tag/Woche handeln.");
        player.sendMessage("");
        player.sendMessage(ChatManager.color("#71C75D/quest event #4B91FB→ Zeigt heutige Specials der Händler an"));
        player.sendMessage(ChatManager.color("#71C75D/quest daily #4B91FB→ Zeigt deinen Tagesfortschritt an"));
        player.sendMessage(ChatManager.color("#71C75D/quest stats #4B91FB→ Zeigt deine Leistungsfortschritte im Questsystem an"));
        player.sendMessage(ChatManager.color("#71C75D/quest top #4B91FB→ Zeigt die Topspieler im Questsystem an"));
        player.sendMessage(ChatManager.color("#71C75D/quest trader #4B91FB→ Zeigt eine Liste mit allen Händlern an"));
        player.sendMessage("");
    }
}
