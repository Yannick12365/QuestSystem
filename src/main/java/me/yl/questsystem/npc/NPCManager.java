package me.yl.questsystem.npc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.yl.questsystem.main;

import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class NPCManager {
    private static final List<NPC> NPCList = new ArrayList<>();

    public List<NPC> getNPClist(){
        return NPCList;
    }

    public void createNPC(Player p, String n) {
        NPC npcFound = checkForNPC(n);
        if (npcFound == null) {
            DedicatedServer server = ((CraftServer) Bukkit.getServer()).getServer();

            WorldServer world = ((CraftWorld) Objects.requireNonNull(Bukkit.getWorld(p.getWorld().getName()))).getHandle();
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), n);

            EntityPlayer entityPlayer = new EntityPlayer(server, world, gameProfile, null);
            entityPlayer.b(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());

            NPC npc = new NPC(entityPlayer, gameProfile, world, n,true, true, "Neue Quests immer am Montag um 05:00 Uhr");

            sentNPCPacket(npc.getEntityplayer());
            NPCList.add(npc);
            new NPCConfigManager().writeNPCConfig(npc, new String[]{"create","create"});
        } else {
            p.sendMessage("Ein NPC mit diesem Namen existiert schon!");
        }
    }

    private void sentNPCPacket(EntityPlayer npc) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
            connection.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, npc));
            connection.a(new PacketPlayOutNamedEntitySpawn(npc));
            connection.a(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.getBukkitYaw() * 256 /360)));

            fixSkin(connection, npc);

            removeTablist(connection, npc);
        }
    }

    public void sentJoinPacket(Player player) {
        for (NPC npc : NPCList) {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
            connection.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, npc.getEntityplayer()));
            connection.a(new PacketPlayOutNamedEntitySpawn(npc.getEntityplayer()));
            connection.a(new PacketPlayOutEntityHeadRotation(npc.getEntityplayer(), (byte) (npc.getEntityplayer().getBukkitYaw() * 256 /360)));

            fixSkin(connection, npc.getEntityplayer());

            removeTablist(connection, npc.getEntityplayer());
        }
    }

    private void fixSkin(PlayerConnection connection, EntityPlayer npc){
        Byte secondOverlay = (0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40);

        DataWatcher watcher = npc.ai();
        watcher.b(new DataWatcherObject<Byte>(17, DataWatcherRegistry.a), secondOverlay);
        connection.a(new PacketPlayOutEntityMetadata(npc.ae(), watcher, true));
    }

    public void removeTablist(PlayerConnection connection, EntityPlayer npc){
        main mainPlugin = main.getPlugin(main.class);
        new BukkitRunnable() {
            int counter = 0;
            @Override
            public void run() {
                if (counter == 1){
                    connection.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, npc));
                    cancel();
                }
                counter++;
            }
        }.runTaskTimer(mainPlugin, 0, 20);
    }

    public void removeNPC(Player p, String n) {
        NPC npcFound = checkForNPC(n);

        if (npcFound != null) {
            EntityPlayer entityPlayer = npcFound.getEntityplayer();
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
                connection.a(new PacketPlayOutEntityDestroy(entityPlayer.getBukkitEntity().getEntityId()));
            }
            NPCList.remove(npcFound);
            new NPCConfigManager().deleteNPCInConfig(n);
        } else {
            p.sendMessage("Ein NPC mit diesem Namen existiert schon!");
        }
    }

    public void tpNPC(Player p, String n) {
        for (NPC npcFound : NPCList) {
            if (npcFound.getName().equalsIgnoreCase(n)) {
                npcFound.getEntityplayer().b(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());
                sentNPCPacket(npcFound.getEntityplayer());
                new NPCConfigManager().writeNPCConfig(npcFound,new String[]{"tp","tp"});
                return;
            }
        }
        p.sendMessage("Ein NPC mit diesem Namen existiert schon!");
    }

    public void changeNPCSkin(Player p, String n, String skin) {
        for (NPC npcFound : NPCList) {
            if (npcFound.getName().equalsIgnoreCase(n)) {
                String[] npcSkin = getSkin(p, skin);
                npcFound.getGameProfile().getProperties().clear();
                if (!(npcSkin[0].equalsIgnoreCase("") && npcSkin[1].equalsIgnoreCase(""))) {
                    npcFound.getGameProfile().getProperties().put("textures", new Property("textures", npcSkin[0], npcSkin[1]));
                }
                sentNPCPacket(npcFound.getEntityplayer());
                new NPCConfigManager().writeNPCConfig(npcFound, npcSkin);

                return;
            }
        }
        p.sendMessage("NPC not found!");
    }

    private String[] getSkin(Player p, String skin) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + skin);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            String uuid = JsonParser.parseReader(reader).getAsJsonObject().get("id").getAsString();

            URL url2 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            InputStreamReader reader2 = new InputStreamReader(url2.openStream());
            JsonObject property = JsonParser.parseReader(reader2).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            String texture = property.get("value").getAsString();
            String signature = property.get("signature").getAsString();
            return new String[]{texture, signature};
        } catch (Exception e) {
            p.sendMessage("Der Skin vom angegebenen Spieler konnte nicht geladen werden!");
            return new String[]{"", ""};
        }
    }

    public void createAfterRestart(Map<String,String > npcData){
        Location location = new Location(Bukkit.getWorld(npcData.get("World")), Double.parseDouble(npcData.get("X")), Double.parseDouble(npcData.get("Y")), Double.parseDouble(npcData.get("Z")), Float.parseFloat(npcData.get("Yaw")), Float.parseFloat(npcData.get("Pitch")));
        String name = npcData.get("Name");
        DedicatedServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) Objects.requireNonNull(location.getWorld())).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.fromString(npcData.get("UUID")), npcData.get("Name"));
        Boolean status = Boolean.parseBoolean(npcData.get("Status"));
        Boolean wochenbonus = Boolean.parseBoolean(npcData.get("WochenBonus"));
        String anmerkungen = npcData.get("Anmerkungen");

        EntityPlayer entityPlayer = new EntityPlayer(server, world, gameProfile, null);
        entityPlayer.b(Double.parseDouble(npcData.get("X")), Double.parseDouble(npcData.get("Y")), Double.parseDouble(npcData.get("Z")), Float.parseFloat(npcData.get("Yaw")), Float.parseFloat(npcData.get("Pitch")));

        NPC npc = new NPC(entityPlayer, gameProfile, world, name, status, wochenbonus, anmerkungen);

        if (!(npcData.get("SkinValue").equals("create") && (npcData.get("SkinSignature")).equals("create")) || !(npcData.get("SkinValue").equals("tp") && (npcData.get("SkinSignature")).equals("tp"))) {
            npc.getGameProfile().getProperties().put("textures", new Property("textures", npcData.get("SkinValue"), npcData.get("SkinSignature")));
        }
        NPCList.add(npc);
    }

    public NPC checkForNPC(String n){
        for(NPC npcFound: NPCList){
            if (npcFound.getName().equalsIgnoreCase(n)){
                return npcFound;
            }
        }
        return null;
    }

    public void changeNPCStatus(NPC npc, boolean status){
        for (NPC npcFound: NPCList) {
            if (npc == npcFound){
                npcFound.setStatus(status);
                new NPCConfigManager().changeNPCStatusInConfig(npc.getName(),status);
            }
        }
    }

    public void changeNPCWochenbonus(NPC npc, boolean status){
        for (NPC npcFound: NPCList) {
            if (npc == npcFound){
                npcFound.setWochenBonus(status);
                new NPCConfigManager().changeNPCWochenbonusInConfig(npc.getName(),status);
            }
        }
    }
}
