package me.yl.questsystem.manager;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import me.oxolotel.utils.bukkit.menuManager.InventoryMenuManager;
import me.yl.questsystem.main;
import me.yl.questsystem.npc.NPC;
import me.yl.questsystem.npc.NPCManager;
import me.yl.questsystem.quest.QuestManager;
import me.yl.questsystem.quest.editMenu.EditQuestMenu;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;

public class ProtocolLibReader {
    public void readNPCClickPacket(ProtocolManager pm, main main) {
        if (pm != null) {
            pm.addPacketListener(new PacketAdapter(main, PacketType.Play.Client.USE_ENTITY) {
                @Override
                public void onPacketReceiving(PacketEvent event) {
                    PacketContainer packet = event.getPacket();

                    for (NPC npc : new NPCManager().getNPClist()) {
                        if (packet.getIntegers().read(0) == npc.getEntityplayer().ae()) {
                            try {
                                EnumWrappers.Hand hand = packet.getEnumEntityUseActions().read(0).getHand();
                                EnumWrappers.EntityUseAction action = packet.getEnumEntityUseActions().read(0).getAction();
                                if (hand == EnumWrappers.Hand.MAIN_HAND && action == EnumWrappers.EntityUseAction.INTERACT) {
                                    new BukkitRunnable() {
                                        int counter = 0;
                                        @Override
                                        public void run() {
                                            if (counter == 1){
                                                int countSlots = new QuestManager().getEditQuestGUISize(npc);

                                                InventoryMenuManager.getInstance().openMenu(event.getPlayer(), new EditQuestMenu(countSlots, npc));
                                                cancel();
                                            }
                                            counter++;
                                        }
                                    }.runTaskTimer(main, 0, 1);
                                }
                            }catch (IllegalArgumentException exception){
                                event.getPlayer().sendMessage("Left Click");
                            }
                        }
                    }
                }
            });
        }
    }


    private HashMap<UUID,String> resultNames = new HashMap<>();

    public HashMap<UUID,String> getAnvilInput(){
        return resultNames;
    }

    public void readWindowClickPacket(ProtocolManager pm, main main){
        if (pm != null) {
            pm.addPacketListener(new PacketAdapter(main, PacketType.Play.Client.WINDOW_CLICK) {
                @Override
                public void onPacketReceiving(PacketEvent event) {
                    Inventory invFound = null;
                    for (Inventory inv: new AnvilMenuManager().getInvList()){
                        for (HumanEntity viewer: inv.getViewers()){
                            if (viewer == event.getPlayer()){
                                invFound = inv;
                            }
                        }
                    }

                    if (invFound != null){
                        PacketContainer packet = event.getPacket();

                        if (packet.getIntegers().read(2) == 2) {

                            event.getPlayer().sendMessage(packet.getItemModifier().read(0).getItemMeta().getDisplayName());
                            resultNames.put(event.getPlayer().getUniqueId(),packet.getItemModifier().read(0).getItemMeta().getDisplayName());

                            PacketContainer container = new PacketContainer(PacketType.Play.Server.CLOSE_WINDOW);
                            try {
                                pm.sendServerPacket(event.getPlayer(), container);
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

        }
    }


}