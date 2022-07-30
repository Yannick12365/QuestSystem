package me.yl.questsystem.manager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatManager {
    private final static Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    public static String color(String msg) {
        Matcher match = pattern.matcher(msg);
        while (match.find()) {
            String color = msg.substring(match.start(), match.end());
            msg = msg.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
            match = pattern.matcher(msg);
        }
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', msg);
    }
}
