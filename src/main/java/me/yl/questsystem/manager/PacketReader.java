package me.yl.questsystem.manager;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import me.oxolotel.utils.bukkit.menuManager.InventoryMenuManager;

import me.yl.questsystem.main;
import me.yl.questsystem.npc.NPC;
import me.yl.questsystem.npc.NPCManager;
import me.yl.questsystem.quest.QuestManager;
import me.yl.questsystem.quest.editMenu.EditQuestMenu;

import net.minecraft.network.protocol.Packet;

import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.List;

public class PacketReader {
    private final Player player;
    private Channel channel;
    private NPC npcFound;

    public PacketReader(Player player) {
        this.player = player;
    }

    public void inject(){
        CraftPlayer cPlayer = (CraftPlayer)this.player;
        channel = cPlayer.getHandle().b.a().m;
        channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<Packet<?>>() {
            @Override protected void decode(ChannelHandlerContext arg0, Packet<?> packet, List<Object> arg2) throws Exception {
                arg2.add(packet);
                readPacket(packet);
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
                            int countSlots = new QuestManager().getEditQuestGUISize(npcFound);

                            InventoryMenuManager.getInstance().openMenu(player, new EditQuestMenu(countSlots, npcFound));
                            cancel();
                        }
                        counter++;
                    }
                }.runTaskTimer(mainPlugin, 0, 1);
            }
        } else if (packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInWindowClick")) {
            player.sendMessage("Click :)");
        }
    }

    public Object getValue(Object obj,String name){
        try{
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(obj);
        }catch(Exception ignored){}
        return null;
    }
}
