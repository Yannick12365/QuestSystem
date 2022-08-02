package me.yl.questsystem.npc;

import me.yl.questsystem.main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        ArrayList<String> npcStringList = new ArrayList<>();
        if(npcFileConf.contains("NPCList")){
            if (!(npcFileConf.getStringList("NPCList").contains(npc.getName(npc)))) {
                npcStringList = (ArrayList<String>) npcFileConf.getStringList("NPCList");
                npcStringList.add(npc.getName(npc));
                npcFileConf.set("NPCList", npcStringList);
            }
        }else{
            npcStringList.add(npc.getName(npc));
            npcFileConf.set("NPCList", npcStringList);
        }

        if (skin[0] == "create" && skin[0] == "create") {
            npcFileConf.set(npc.getName(npc)+".Name",npc.getName(npc));
            npcFileConf.set(npc.getName(npc)+".X",Double.toString(npc.getEntityplayer(npc).getBukkitEntity().getLocation().getX()));
            npcFileConf.set(npc.getName(npc)+".Y",Double.toString(npc.getEntityplayer(npc).getBukkitEntity().getLocation().getY()));
            npcFileConf.set(npc.getName(npc)+".Z",Double.toString(npc.getEntityplayer(npc).getBukkitEntity().getLocation().getZ()));
            npcFileConf.set(npc.getName(npc)+".Yaw",Double.toString(npc.getEntityplayer(npc).getBukkitEntity().getLocation().getYaw()));
            npcFileConf.set(npc.getName(npc)+".Pitch",Double.toString(npc.getEntityplayer(npc).getBukkitEntity().getLocation().getPitch()));
            npcFileConf.set(npc.getName(npc)+".UUID",npc.getGameProfile(npc).getId().toString());
            npcFileConf.set(npc.getName(npc)+".World",npc.getWorld(npc).getWorld().getName());
            npcFileConf.set(npc.getName(npc) + ".SkinValue", "Kein Skin");
            npcFileConf.set(npc.getName(npc) + ".SkinSignature", "Kein Skin");
        }else if(skin[0] == "tp" && skin[0] == "tp"){
            npcFileConf.set(npc.getName(npc)+".X",Double.toString(npc.getEntityplayer(npc).getBukkitEntity().getLocation().getX()));
            npcFileConf.set(npc.getName(npc)+".Y",Double.toString(npc.getEntityplayer(npc).getBukkitEntity().getLocation().getY()));
            npcFileConf.set(npc.getName(npc)+".Z",Double.toString(npc.getEntityplayer(npc).getBukkitEntity().getLocation().getZ()));
            npcFileConf.set(npc.getName(npc)+".Yaw",Double.toString(npc.getEntityplayer(npc).getBukkitEntity().getLocation().getYaw()));
            npcFileConf.set(npc.getName(npc)+".Pitch",Double.toString(npc.getEntityplayer(npc).getBukkitEntity().getLocation().getPitch()));
        } else {
            npcFileConf.set(npc.getName(npc) + ".SkinValue", skin[0]);
            npcFileConf.set(npc.getName(npc) + ".SkinSignature", skin[1]);
        }

        try {
            npcFileConf.save(npcFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readNPCConfig(){
        if(npcFileConf.contains("NPCList")) {
            ArrayList<String> npcStringList = (ArrayList<String>) npcFileConf.getStringList("NPCList");

            if (npcStringList.size() > 0) {
                Map<String, String> npcData = new HashMap<>();

                for (String npcPlayer : npcStringList) {
                    npcData.put("Name", npcFileConf.getString(npcPlayer+".Name"));
                    npcData.put("X", npcFileConf.getString(npcPlayer+".X"));
                    npcData.put("Y", npcFileConf.getString(npcPlayer+".Y"));
                    npcData.put("Z", npcFileConf.getString(npcPlayer+".Z"));
                    npcData.put("Yaw", npcFileConf.getString(npcPlayer+".Yaw"));
                    npcData.put("Pitch", npcFileConf.getString(npcPlayer+".Pitch"));
                    npcData.put("UUID", npcFileConf.getString(npcPlayer+".UUID"));
                    npcData.put("World", npcFileConf.getString(npcPlayer+".World"));
                    npcData.put("SkinValue", npcFileConf.getString(npcPlayer+".SkinValue"));
                    npcData.put("SkinSignature", npcFileConf.getString(npcPlayer+".SkinSignature"));

                    new NPCManager().createAfterRestart(npcData);
                }
            }
        }
    }

    public void deleteNPCInConfig(String n){
        ArrayList<String> npcList = (ArrayList<String>) npcFileConf.getStringList("NPCList");
        npcList.remove(n);
        npcFileConf.set("NPCList",npcList);
        npcFileConf.set(n,null);
        try {
            npcFileConf.save(npcFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}