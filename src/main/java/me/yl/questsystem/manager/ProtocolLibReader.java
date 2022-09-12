package me.yl.questsystem.manager;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import me.oxolotel.utils.bukkit.menuManager.InventoryMenuManager;
import me.oxolotel.utils.bukkit.menuManager.implement.MenuView;
import me.oxolotel.utils.bukkit.menuManager.menus.Closeable;
import me.oxolotel.utils.bukkit.menuManager.menus.CustomMenu;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryContent;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryItem;
import me.oxolotel.utils.general.ReflectionUtils;
import me.oxolotel.utils.general.TrippleWrapper;
import me.oxolotel.utils.wrapped.schedule.Task;
import me.yl.questsystem.main;
import me.yl.questsystem.npc.NPC;
import me.yl.questsystem.npc.NPCManager;
import me.yl.questsystem.quest.Quest;
import me.yl.questsystem.quest.QuestManager;
import me.yl.questsystem.quest.editMenu.EditQuestMenu;
import me.yl.questsystem.quest.editMenu.ReeditQuestMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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


    public void readWindowClickPacket(ProtocolManager pm, main main){
        if (pm != null) {
            pm.addPacketListener(new PacketAdapter(main, PacketType.Play.Client.WINDOW_CLICK) {
                @Override
                public void onPacketReceiving(PacketEvent event) {
                    Inventory invFound = null;
                    Player p = event.getPlayer();
                    for (Inventory inv: new AnvilMenuManager().getInvList()){
                        for (HumanEntity viewer: inv.getViewers()){
                            if (viewer == p){
                                invFound = inv;
                            }
                        }
                    }

                    if (invFound != null){
                        PacketContainer packet = event.getPacket();

                        if (packet.getIntegers().read(2) == 2) {
                            boolean menge = false;
                            ItemStack item = packet.getItemModifier().read(0);
                            String input = packet.getItemModifier().read(0).getItemMeta().getDisplayName();

                            if(p.getOpenInventory().getTitle().equalsIgnoreCase("Edit Quest Menge")){
                                menge = true;
                            }
                            PacketContainer container = new PacketContainer(PacketType.Play.Server.CLOSE_WINDOW);
                            try {
                                pm.sendServerPacket(p, container);
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }

                            boolean finalMenge = menge;
                            new BukkitRunnable() {
                                int counter = 0;
                                @Override
                                public void run() {
                                    if (counter == 1){

                                        for (CustomMenu cm : ReeditQuestMenu.getOpenMenus().get(p.getUniqueId())){
                                                InventoryMenuManager.getInstance().openMenu(p, cm);
                                        }
                                        if (finalMenge) {

                                            InventoryMenuManager.getInstance().getOpenMenu(p).getCurrentInventoryContent().addGuiItem(28, new InventoryItem(new ItemManager(Material.OAK_SIGN).setDisplayName("Item Menge").setLore((input)).build(), ()->{
                                                ItemStack tempItem = new ItemManager(item.getType()).setDisplayName(" ").build();
                                                new ReeditQuestMenu().saveMenus(p);
                                                InventoryMenuManager.getInstance().closeMenu(p, Closeable.CloseReason.CHANGEMENU);
                                                new AnvilMenuManager().createAnvilMenu(p ,tempItem,"Edit Quest Menge");
                                            }));
                                        }else {
                                            InventoryMenuManager.getInstance().getOpenMenu(p).getCurrentInventoryContent().addGuiItem(30, new InventoryItem(new ItemManager(Material.OAK_SIGN).setDisplayName("Item Preis").setLore(((input))).build(), ()->{
                                                ItemStack item = new ItemManager(Material.GOLD_INGOT).setDisplayName(" ").build();
                                                new ReeditQuestMenu().saveMenus(p);
                                                InventoryMenuManager.getInstance().closeMenu(p, Closeable.CloseReason.CHANGEMENU);
                                                new AnvilMenuManager().createAnvilMenu(p ,item,"Edit Quest Preis");
                                            }));

                                        }
                                        InventoryMenuManager.getInstance().getOpenMenu(p).refresh();
                                        cancel();
                                    }
                                    counter++;
                                }
                            }.runTaskTimer(main, 0, 1);
                        }
                    }
                }
            });

        }
    }


}