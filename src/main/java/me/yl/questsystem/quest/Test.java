package me.yl.questsystem.quest;

import me.oxolotel.utils.bukkit.menuManager.InventoryMenuManager;
import org.bukkit.entity.Player;

public class Test {

    Player p;

    public Test(Player p){
        this.p = p;
        InventoryMenuManager.getInstance().openMenu(p, new Menu(54));

    }


}
