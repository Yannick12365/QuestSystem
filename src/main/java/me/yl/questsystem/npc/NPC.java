package me.yl.questsystem.npc;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;

public class NPC {
    private final EntityPlayer npc;
    private final GameProfile gameProfile;
    private final WorldServer world;
    private final String name;
    private String anmerkungen;
    private boolean wochenBonus;
    private boolean status;

    public NPC(EntityPlayer npc, GameProfile gameProfile, WorldServer world, String name, boolean status, boolean wochenBonus, String anmerkungen){
        this.npc = npc;
        this.gameProfile = gameProfile;
        this. world = world;
        this.name = name;
        this.anmerkungen = anmerkungen;
        this.wochenBonus = wochenBonus;
        this.status = status;
    }

    public String getName(){
        return name;
    }
    public WorldServer getWorld(){
        return world;
    }
    public GameProfile getGameProfile(){
        return gameProfile;
    }
    public EntityPlayer getEntityplayer(){
        return npc;
    }

    public String getAnmerkungen() {
        return anmerkungen;
    }

    public void setAnmerkungen(String anmerkungen) {
        this.anmerkungen = anmerkungen;
    }

    public boolean isWochenBonus() {
        return wochenBonus;
    }

    public void setWochenBonus(boolean wochenBonus) {
        this.wochenBonus = wochenBonus;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
