package me.yl.questsystem.npc;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;

public class NPC {
    private final EntityPlayer npc;
    private final GameProfile gameProfile;
    private final WorldServer world;
    private final String name;

    public NPC(EntityPlayer npc, GameProfile gameProfile, WorldServer world, String name){
        this.npc = npc;
        this.gameProfile = gameProfile;
        this. world = world;
        this.name = name;
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
}
