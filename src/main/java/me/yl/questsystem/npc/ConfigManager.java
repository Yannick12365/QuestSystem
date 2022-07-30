package me.yl.questsystem.npc;

import me.yl.questsystem.main;
import me.yl.questsystem.npc.NPCManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    public static File npcFile;
    public static FileConfiguration npcFileconf;

    public void createConfigConfiguration(main main){
        if (!main.getDataFolder().exists()){
            main.getDataFolder().mkdir();
        }
        npcFile = new File(main.getDataFolder(), "npc.yml");
        if (!npcFile.exists()){
            main.saveResource("npc.yml",false);
        }
        npcFileconf = YamlConfiguration.loadConfiguration(npcFile);
    }

    public void writeNPCconfig(NPCManager npcutil, String[] skin){
        ArrayList<String> npcStringList = new ArrayList<>();
        if(npcFileconf.contains("NPCList")){
            if (!(npcFileconf.getStringList("NPCList").contains(npcutil.getName(npcutil)))) {
                npcStringList = (ArrayList<String>) npcFileconf.getStringList("NPCList");
                npcStringList.add(npcutil.getName(npcutil));
                npcFileconf.set("NPCList", npcStringList);
            }
        }else{
            npcStringList.add(npcutil.getName(npcutil));
            npcFileconf.set("NPCList", npcStringList);
        }

        if (skin[0] == "create" && skin[0] == "create") {
            npcFileconf.set(npcutil.getName(npcutil)+".Name",npcutil.getName(npcutil));
            npcFileconf.set(npcutil.getName(npcutil)+".X",Double.toString(npcutil.getEntityplayer(npcutil).getBukkitEntity().getLocation().getX()));
            npcFileconf.set(npcutil.getName(npcutil)+".Y",Double.toString(npcutil.getEntityplayer(npcutil).getBukkitEntity().getLocation().getY()));
            npcFileconf.set(npcutil.getName(npcutil)+".Z",Double.toString(npcutil.getEntityplayer(npcutil).getBukkitEntity().getLocation().getZ()));
            npcFileconf.set(npcutil.getName(npcutil)+".Yaw",Double.toString(npcutil.getEntityplayer(npcutil).getBukkitEntity().getLocation().getYaw()));
            npcFileconf.set(npcutil.getName(npcutil)+".Pitch",Double.toString(npcutil.getEntityplayer(npcutil).getBukkitEntity().getLocation().getPitch()));
            npcFileconf.set(npcutil.getName(npcutil)+".UUID",npcutil.getGameProfile(npcutil).getId().toString());
            npcFileconf.set(npcutil.getName(npcutil)+".World",npcutil.getWorld(npcutil).getWorld().getName());
            npcFileconf.set(npcutil.getName(npcutil) + ".SkinValue", "Kein Skin");
            npcFileconf.set(npcutil.getName(npcutil) + ".SkinSignature", "Kein Skin");
        }else if(skin[0] == "tp" && skin[0] == "tp"){
            npcFileconf.set(npcutil.getName(npcutil)+".X",Double.toString(npcutil.getEntityplayer(npcutil).getBukkitEntity().getLocation().getX()));
            npcFileconf.set(npcutil.getName(npcutil)+".Y",Double.toString(npcutil.getEntityplayer(npcutil).getBukkitEntity().getLocation().getY()));
            npcFileconf.set(npcutil.getName(npcutil)+".Z",Double.toString(npcutil.getEntityplayer(npcutil).getBukkitEntity().getLocation().getZ()));
            npcFileconf.set(npcutil.getName(npcutil)+".Yaw",Double.toString(npcutil.getEntityplayer(npcutil).getBukkitEntity().getLocation().getYaw()));
            npcFileconf.set(npcutil.getName(npcutil)+".Pitch",Double.toString(npcutil.getEntityplayer(npcutil).getBukkitEntity().getLocation().getPitch()));
        } else {
            npcFileconf.set(npcutil.getName(npcutil) + ".SkinValue", skin[0]);
            npcFileconf.set(npcutil.getName(npcutil) + ".SkinSignature", skin[1]);
        }

        try {
            npcFileconf.save(npcFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readNPCconfig(){
        if(npcFileconf.contains("NPCList")) {
            ArrayList<String> npcStringList = (ArrayList<String>) npcFileconf.getStringList("NPCList");

            if (npcStringList.size() > 0) {
                Map<String, String> npcData = new HashMap<>();

                for (String npcPlayer : npcStringList) {
                    npcData.put("Name",npcFileconf.getString(npcPlayer+".Name"));
                    npcData.put("X",npcFileconf.getString(npcPlayer+".X"));
                    npcData.put("Y",npcFileconf.getString(npcPlayer+".Y"));
                    npcData.put("Z",npcFileconf.getString(npcPlayer+".Z"));
                    npcData.put("Yaw",npcFileconf.getString(npcPlayer+".Yaw"));
                    npcData.put("Pitch",npcFileconf.getString(npcPlayer+".Pitch"));
                    npcData.put("UUID",npcFileconf.getString(npcPlayer+".UUID"));
                    npcData.put("World",npcFileconf.getString(npcPlayer+".World"));
                    npcData.put("SkinValue",npcFileconf.getString(npcPlayer+".SkinValue"));
                    npcData.put("SkinSignature",npcFileconf.getString(npcPlayer+".SkinSignature"));

                    new NPCManager().createAfterRestart(npcData);
                }
            }
        }
    }

    public void deleteNPCInconfig(String n){
        ArrayList<String> npcList = (ArrayList<String>) npcFileconf.getStringList("NPCList");
        npcList.remove(n);
        npcFileconf.set("NPCList",npcList);
        npcFileconf.set(n,null);
        try {
            npcFileconf.save(npcFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}