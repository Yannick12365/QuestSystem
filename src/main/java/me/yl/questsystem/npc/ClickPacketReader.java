package me.yl.questsystem.npc;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import me.oxolotel.utils.bukkit.menuManager.InventoryMenuManager;
import me.yl.questsystem.main;
import me.yl.questsystem.quest.QuestManager;
import net.minecraft.network.protocol.Packet;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import me.yl.questsystem.quest.editMenu.EditQuestMenu;
import java.lang.reflect.Field;
import java.util.List;

public class ClickPacketReader {
    private Player player;
    private Channel channel;
    private NPC npcFound;

    public ClickPacketReader(Player player) {
        this.player = player;
    }

    public void inject(){
        CraftPlayer cPlayer = (CraftPlayer)this.player;
        channel = cPlayer.getHandle().b.a().m;
        channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<Packet<?>>() {
            @Override protected void decode(ChannelHandlerContext arg0, Packet<?> packet, List<Object> arg2) throws Exception {
                arg2.add(packet);readPacket(packet);
            }
        });
    }

    public void uninject(){

        if(channel.pipeline().get("PacketInjector") != null){
            channel.pipeline().remove("PacketInjector");
        }
    }

    public void readPacket(Packet<?> packet){
        if(packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")){
            int id = (Integer)getValue(packet, "a");

            npcFound = null;
            for(NPC npc:new NPCManager().getNPClist()){
                if (npc.getEntityplayer().ae() == id){
                    npcFound = npc;
                }
            }
            if(npcFound != null){
                main mainPlugin = main.getPlugin(main.class);
                new BukkitRunnable() {
                    int counter = 0;
                    @Override
                    public void run() {
                        if (counter == 1){
                            int questCount = (new QuestManager().countNPCQuests(npcFound.getName()));
                            if (questCount == 0){
                                questCount = 1;
                            }
                            int countSlots;

                            if (questCount % 21 == 0){
                                countSlots = (questCount/21*54) - (questCount/21*9);
                            }else{
                                countSlots = (((questCount/21)+1)*54) - (((questCount/21)+1)*9);
                            }
                            InventoryMenuManager.getInstance().openMenu(player, new EditQuestMenu(countSlots, npcFound.getName()));
                            cancel();
                        }
                        counter++;
                    }
                }.runTaskTimer(mainPlugin, 0, 1);
            }
        }
    }

    public Object getValue(Object obj,String name){
        try{
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(obj);
        }catch(Exception e){}
        return null;
    }
}
