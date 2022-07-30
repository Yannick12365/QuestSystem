package me.yl.questsystem.manager;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.yl.questsystem.main;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.MinecraftServer;
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
    private static final List<NPCManager> NPC = new ArrayList<>();
    private EntityPlayer npc;
    private GameProfile gameProfile;
    private WorldServer world;
    private MinecraftServer server;
    private String name;


    public void createNPC(Player p, String n) {
        boolean exist = false;
        for (NPCManager existNPC : NPC) {
            if (existNPC.name.equalsIgnoreCase(n) || n.equalsIgnoreCase("NPCList")) {
                exist = true;
            }
        }
        if (exist == false) {
            this.name = n;
            this.server = ((CraftServer) Bukkit.getServer()).getServer();
            this.world = ((CraftWorld) Objects.requireNonNull(Bukkit.getWorld(p.getWorld().getName()))).getHandle();
            this.gameProfile = new GameProfile(UUID.randomUUID(), n);

            this.npc = new EntityPlayer(this.server, world, gameProfile, null);
            this.npc.b(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());

            sentNPCPacket(this.npc);

            NPC.add(this);
            new ConfigManager().writeNPCconfig(this, new String[]{"create","create"});
        } else {
            p.sendMessage("Ein NPC mit diesem Namen existiert schon!");
        }
    }

    private void sentNPCPacket(EntityPlayer npc) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
            connection.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, npc));
            connection.a(new PacketPlayOutNamedEntitySpawn(npc));

            fixSkin(connection, npc);

            removeTablist(connection, npc);
        }
    }

    public void sentJoinPacket(Player player) {
        for (NPCManager npc : NPC) {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
            connection.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, npc.npc));
            connection.a(new PacketPlayOutNamedEntitySpawn(npc.npc));

            fixSkin(connection, npc.npc);

            removeTablist(connection, npc.npc);
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
        EntityPlayer entityPlayer = null;
        NPCManager existNPC2 = null;

        for (NPCManager existNPC : NPC) {
            if (existNPC.name.equalsIgnoreCase(n)) {
                existNPC2 = existNPC;
                entityPlayer = existNPC.npc;
            }
        }

        if (entityPlayer != null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
                connection.a(new PacketPlayOutEntityDestroy(entityPlayer.getBukkitEntity().getEntityId()));
            }
            NPC.remove(existNPC2);
            new ConfigManager().deleteNPCInconfig(n);
        } else {
            p.sendMessage("Ein NPC mit diesem Namen existiert schon!");
        }
    }

    public void tpNPC(Player p, String n) {
        for (NPCManager existNPC : NPC) {
            if (existNPC.name.equalsIgnoreCase(n)) {
                existNPC.npc.b(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());
                sentNPCPacket(existNPC.npc);
                new ConfigManager().writeNPCconfig(existNPC,new String[]{"tp","tp"});
                return;
            }
        }
        p.sendMessage("Ein NPC mit diesem Namen existiert schon!");
    }

    public void changeNPCSkin(Player p, String n, String skin) {
        for (NPCManager existNPC : NPC) {
            if (existNPC.name.equalsIgnoreCase(n)) {
                String[] npcSkin = getSkin(p, skin);
                existNPC.gameProfile.getProperties().clear();
                if (!(npcSkin[0].equalsIgnoreCase("") && npcSkin[0].equalsIgnoreCase(""))) {
                    existNPC.gameProfile.getProperties().put("textures", new Property("textures", npcSkin[0], npcSkin[1]));
                }
                sentNPCPacket(existNPC.npc);
                new ConfigManager().writeNPCconfig(existNPC, npcSkin);

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
        this.name = npcData.get("Name");
        this.server = ((CraftServer) Bukkit.getServer()).getServer();
        this.world = ((CraftWorld) location.getWorld()).getHandle();
        this.gameProfile = new GameProfile(UUID.fromString(npcData.get("UUID")), npcData.get("Name"));

        this.npc = new EntityPlayer(this.server, world, gameProfile, null);
        this.npc.b(Double.parseDouble(npcData.get("X")), Double.parseDouble(npcData.get("Y")), Double.parseDouble(npcData.get("Z")), Float.parseFloat(npcData.get("Yaw")), Float.parseFloat(npcData.get("Pitch")));

        if (!(npcData.get("SkinValue") == "create" && (npcData.get("SkinSignature")) == "create") || !(npcData.get("SkinValue") == "tp" && (npcData.get("SkinSignature")) == "tp")) {
            this.gameProfile.getProperties().put("textures", new Property("textures", npcData.get("SkinValue"), npcData.get("SkinSignature")));
        }
        NPC.add(this);
    }

    public String getName(NPCManager npcUtil){
        return npcUtil.name;
    }
    public WorldServer getWorld(NPCManager npcUtil){
        return npcUtil.world;
    }
    public GameProfile getGameProfile(NPCManager npcUtil){
        return npcUtil.gameProfile;
    }
    public EntityPlayer getEntityplayer(NPCManager npcUtil){
        return npcUtil.npc;
    }
    public List<NPCManager> getNPClist(){
        return NPC;
    }
}
