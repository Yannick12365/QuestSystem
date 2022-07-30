package me.yl.questsystem.npc;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;

import java.util.ArrayList;
import java.util.List;

public class NPC {
    private EntityPlayer npc;
    private GameProfile gameProfile;
    private WorldServer world;
    private MinecraftServer server;
    private String name;

    public NPC(){}

    public NPC(EntityPlayer npc, GameProfile gameProfile, WorldServer world, MinecraftServer server, String name){
        this.npc = npc;
        this.gameProfile = gameProfile;
        this. world = world;
        this.server = server;
        this.name = name;
    }


    public String getName(NPC npc){
        return npc.name;
    }
    public WorldServer getWorld(NPC npc){
        return npc.world;
    }
    public GameProfile getGameProfile(NPC npc){
        return npc.gameProfile;
    }
    public EntityPlayer getEntityplayer(NPC npc){
        return npc.npc;
    }

    public MinecraftServer getServer(NPC npc){
        return npc.server;
    }

    public void setEntityplayer(EntityPlayer entityPlayer) {
        this.npc = npc;
    }

    public void setGameProfile(GameProfile gameProfile) {
        this.gameProfile = gameProfile;
    }

    public void setWorld(WorldServer world) {
        this.world = world;
    }

    public void setServer(MinecraftServer server) {
        this.server = server;
    }

    public void setName(String name) {
        this.name = name;
    }
}
