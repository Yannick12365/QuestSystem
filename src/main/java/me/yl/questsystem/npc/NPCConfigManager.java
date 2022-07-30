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

public class NPCConfigManager {
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

    public void writeNPCconfig(NPC npc, String[] skin){
        ArrayList<String> npcStringList = new ArrayList<>();
        if(npcFileconf.contains("NPCList")){
            if (!(npcFileconf.getStringList("NPCList").contains(npc.getName(npc)))) {
                npcStringList = (ArrayList<String>) npcFileconf.getStringList("NPCList");
                npcStringList.add(npc.getName(npc));
                npcFileconf.set("NPCList", npcStringList);
            }
        }else{
            npcStringList.add(npc.getName(npc));
            npcFileconf.set("NPCList", npcStringList);
        }

        if (skin[0] == "create" && skin[0] == "create") {
            npcFileconf.set(npc.getName(npc)+".Name",npc.getName(npc));
            npcFileconf.set(npc.getName(npc)+".X",Double.toString(npc.getEntityplayer(npc).getBukkitEntity().getLocation().getX()));
            npcFileconf.set(npc.getName(npc)+".Y",Double.toString(npc.getEntityplayer(npc).getBukkitEntity().getLocation().getY()));
            npcFileconf.set(npc.getName(npc)+".Z",Double.toString(npc.getEntityplayer(npc).getBukkitEntity().getLocation().getZ()));
            npcFileconf.set(npc.getName(npc)+".Yaw",Double.toString(npc.getEntityplayer(npc).getBukkitEntity().getLocation().getYaw()));
            npcFileconf.set(npc.getName(npc)+".Pitch",Double.toString(npc.getEntityplayer(npc).getBukkitEntity().getLocation().getPitch()));
            npcFileconf.set(npc.getName(npc)+".UUID",npc.getGameProfile(npc).getId().toString());
            npcFileconf.set(npc.getName(npc)+".World",npc.getWorld(npc).getWorld().getName());
            npcFileconf.set(npc.getName(npc) + ".SkinValue", "Kein Skin");
            npcFileconf.set(npc.getName(npc) + ".SkinSignature", "Kein Skin");
        }else if(skin[0] == "tp" && skin[0] == "tp"){
            npcFileconf.set(npc.getName(npc)+".X",Double.toString(npc.getEntityplayer(npc).getBukkitEntity().getLocation().getX()));
            npcFileconf.set(npc.getName(npc)+".Y",Double.toString(npc.getEntityplayer(npc).getBukkitEntity().getLocation().getY()));
            npcFileconf.set(npc.getName(npc)+".Z",Double.toString(npc.getEntityplayer(npc).getBukkitEntity().getLocation().getZ()));
            npcFileconf.set(npc.getName(npc)+".Yaw",Double.toString(npc.getEntityplayer(npc).getBukkitEntity().getLocation().getYaw()));
            npcFileconf.set(npc.getName(npc)+".Pitch",Double.toString(npc.getEntityplayer(npc).getBukkitEntity().getLocation().getPitch()));
        } else {
            npcFileconf.set(npc.getName(npc) + ".SkinValue", skin[0]);
            npcFileconf.set(npc.getName(npc) + ".SkinSignature", skin[1]);
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