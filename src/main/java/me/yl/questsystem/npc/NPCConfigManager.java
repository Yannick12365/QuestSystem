package me.yl.questsystem.npc;

import me.yl.questsystem.main;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NPCConfigManager {
    public static File npcFile;
    public static FileConfiguration npcFileConf;

    public void createConfigConfiguration(main main){
        if (!main.getDataFolder().exists()){
            main.getDataFolder().mkdir();
        }
        npcFile = new File(main.getDataFolder(), "npc.yml");
        if (!npcFile.exists()){
            main.saveResource("npc.yml",false);
        }
        npcFileConf = YamlConfiguration.loadConfiguration(npcFile);
    }

    public void writeNPCConfig(NPC npc, String[] skin){
        if (skin[0].equals("create") && skin[1].equals("create")) {
            npcFileConf.set(npc.getName()+".Name",npc.getName());
            npcFileConf.set(npc.getName()+".X",Double.toString(npc.getEntityplayer().getBukkitEntity().getLocation().getX()));
            npcFileConf.set(npc.getName()+".Y",Double.toString(npc.getEntityplayer().getBukkitEntity().getLocation().getY()));
            npcFileConf.set(npc.getName()+".Z",Double.toString(npc.getEntityplayer().getBukkitEntity().getLocation().getZ()));
            npcFileConf.set(npc.getName()+".Yaw",Double.toString(npc.getEntityplayer().getBukkitEntity().getLocation().getYaw()));
            npcFileConf.set(npc.getName()+".Pitch",Double.toString(npc.getEntityplayer().getBukkitEntity().getLocation().getPitch()));
            npcFileConf.set(npc.getName()+".UUID",npc.getGameProfile().getId().toString());
            npcFileConf.set(npc.getName()+".World",npc.getWorld().getWorld().getName());
            npcFileConf.set(npc.getName() + ".SkinValue", "Kein Skin");
            npcFileConf.set(npc.getName() + ".SkinSignature", "Kein Skin");
            npcFileConf.set(npc.getName()+ ".Anmerkungen", npc.getAnmerkungen());
            npcFileConf.set(npc.getName()+ ".Status", npc.isStatus());
            npcFileConf.set(npc.getName()+ ".WochenBonus", npc.isWochenBonus());
        }else if(skin[0].equals("tp") && skin[1].equals("tp")){
            npcFileConf.set(npc.getName()+".X",Double.toString(npc.getEntityplayer().getBukkitEntity().getLocation().getX()));
            npcFileConf.set(npc.getName()+".Y",Double.toString(npc.getEntityplayer().getBukkitEntity().getLocation().getY()));
            npcFileConf.set(npc.getName()+".Z",Double.toString(npc.getEntityplayer().getBukkitEntity().getLocation().getZ()));
            npcFileConf.set(npc.getName()+".Yaw",Double.toString(npc.getEntityplayer().getBukkitEntity().getLocation().getYaw()));
            npcFileConf.set(npc.getName()+".Pitch",Double.toString(npc.getEntityplayer().getBukkitEntity().getLocation().getPitch()));
        } else {
            npcFileConf.set(npc.getName() + ".SkinValue", skin[0]);
            npcFileConf.set(npc.getName() + ".SkinSignature", skin[1]);
        }

        try {
            npcFileConf.save(npcFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readNPCConfig() {
        Set<String> npcKeys = npcFileConf.getKeys(false);

        if (npcKeys.size() > 0) {
            Map<String, String> npcData = new HashMap<>();

            for (String npcPlayer : npcKeys) {
                npcData.put("Name", npcFileConf.getString(npcPlayer + ".Name"));
                npcData.put("X", npcFileConf.getString(npcPlayer + ".X"));
                npcData.put("Y", npcFileConf.getString(npcPlayer + ".Y"));
                npcData.put("Z", npcFileConf.getString(npcPlayer + ".Z"));
                npcData.put("Yaw", npcFileConf.getString(npcPlayer + ".Yaw"));
                npcData.put("Pitch", npcFileConf.getString(npcPlayer + ".Pitch"));
                npcData.put("UUID", npcFileConf.getString(npcPlayer + ".UUID"));
                npcData.put("World", npcFileConf.getString(npcPlayer + ".World"));
                npcData.put("SkinValue", npcFileConf.getString(npcPlayer + ".SkinValue"));
                npcData.put("SkinSignature", npcFileConf.getString(npcPlayer + ".SkinSignature"));
                npcData.put("Anmerkungen", npcFileConf.getString(npcPlayer + ".Anmerkungen"));
                npcData.put("Status", npcFileConf.getString(npcPlayer + ".Status"));
                npcData.put("WochenBonus", npcFileConf.getString(npcPlayer + ".WochenBonus"));

                new NPCManager().createAfterRestart(npcData);
            }
        }
    }

    public void deleteNPCInConfig(String n){
        npcFileConf.set(n,null);
        try {
            npcFileConf.save(npcFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeNPCStatusInConfig(String n, boolean status){
        npcFileConf.set(n+ ".Status", status);
        try {
            npcFileConf.save(npcFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeNPCWochenbonusInConfig(String n, boolean status){
        npcFileConf.set(n+ ".Status", status);
        try {
            npcFileConf.save(npcFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}