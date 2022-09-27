package me.yl.questsystem.quest.editMenu;

import me.oxolotel.utils.bukkit.menuManager.InventoryMenuManager;
import me.oxolotel.utils.bukkit.menuManager.implement.MenuView;
import me.oxolotel.utils.bukkit.menuManager.menus.*;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryContent;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryItem;
import me.oxolotel.utils.general.ReflectionUtils;
import me.oxolotel.utils.general.TrippleWrapper;
import me.oxolotel.utils.wrapped.Chat;

import me.oxolotel.utils.wrapped.schedule.Task;
import me.yl.questsystem.listener.MenuClick;
import me.yl.questsystem.manager.AnvilMenuManager;
import me.yl.questsystem.manager.ItemManager;
import me.yl.questsystem.manager.ProtocolLibReader;
import me.yl.questsystem.npc.NPC;
import me.yl.questsystem.quest.Quest;
import me.yl.questsystem.quest.QuestManager;
import me.yl.questsystem.main;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


public class ReeditQuestMenu extends CustomMenu implements Closeable, SlotCondition, Modifyable, Submenu, Loggable {

    private InventoryContent content;
    private Player p;
    private NPC npc;
    private Quest quest;
    private static HashMap<UUID, List<CustomMenu>> openMenus = new HashMap<>();
    private int questPacket;


    public ReeditQuestMenu(int size, NPC npc, Quest quest, int nr) {
        super(size);
        setTitle("Quest bearbeiten");
        content = new InventoryContent();
        this.npc = npc;
        this.quest = quest;
        questPacket = nr;
    }

    @Override
    public void onClose(Player player, ItemStack[] itemStacks, CloseReason closeReason) {
        if (closeReason != CloseReason.CHANGEMENU) {
            openMenus.remove(p.getUniqueId());
            new ProtocolLibReader().removeMengeInput(player);
            new ProtocolLibReader().removePreisInput(player);
        }
    }

    @Override
    public InventoryContent getContents(Player player) {
        p = player;

        content.fill(0, 54, new InventoryItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), () -> {
        }));
        content.addGuiItem(13, () -> {
        }, Material.BLUE_DYE, "§9§lBearbeiten");

        content.addGuiItem(28, new InventoryItem(new ItemManager(Material.OAK_SIGN).setDisplayName("Item Menge").setLore(String.valueOf(quest.getItemAmount())).build(), () -> {
                ItemStack item = new ItemManager(quest.getItem().getType()).setDisplayName(" ").build();
                saveMenus(player);
                InventoryMenuManager.getInstance().closeMenu(player, Closeable.CloseReason.CHANGEMENU);
                new AnvilMenuManager().createAnvilMenu(player, item, "Edit Quest Menge");
            }));
            content.addGuiItem(30, new InventoryItem(new ItemManager(Material.OAK_SIGN).setDisplayName("Item Preis").setLore(String.valueOf(quest.getReward())).build(), () -> {
                ItemStack item = new ItemManager(Material.GOLD_INGOT).setDisplayName(" ").build();
                saveMenus(player);
                InventoryMenuManager.getInstance().closeMenu(player, Closeable.CloseReason.CHANGEMENU);
                new AnvilMenuManager().createAnvilMenu(player, item, "Edit Quest Preis");
        }));

        if (getOpenMenus().containsKey(player.getUniqueId())) {
            ProtocolLibReader plr = new ProtocolLibReader();
            if (plr.getMengeInput().containsKey(player.getUniqueId())) {
                content.addGuiItem(28, new InventoryItem(new ItemManager(Material.OAK_SIGN).setDisplayName("Item Menge").setLore(plr.getMengeInput().get(player.getUniqueId())).build(), () -> {
                    ItemStack item = new ItemManager(quest.getItem().getType()).setDisplayName(" ").build();
                    saveMenus(player);
                    InventoryMenuManager.getInstance().closeMenu(player, Closeable.CloseReason.CHANGEMENU);
                    new AnvilMenuManager().createAnvilMenu(player, item, "Edit Quest Menge");
                }));
            }
            if (plr.getPreisInput().containsKey(player.getUniqueId())) {
                content.addGuiItem(30, new InventoryItem(new ItemManager(Material.OAK_SIGN).setDisplayName("Item Preis").setLore(plr.getPreisInput().get(player.getUniqueId())).build(), () -> {
                    ItemStack item = new ItemManager(Material.GOLD_INGOT).setDisplayName(" ").build();
                    saveMenus(player);
                    InventoryMenuManager.getInstance().closeMenu(player, Closeable.CloseReason.CHANGEMENU);
                    new AnvilMenuManager().createAnvilMenu(player, item, "Edit Quest Preis");
                }));
            }
        }

        content.addGuiItem(37, new InventoryItem(quest.getItem(), () -> {
        }));
        content.addGuiItem(38, new InventoryItem(new ItemManager(Material.ARROW).build(), () -> {
        }));
        content.addGuiItem(39, new InventoryItem(new ItemManager(Material.GOLD_INGOT).setDisplayName(" ").build(), () -> {
        }));
        content.addGuiItem(41, new InventoryItem(new ItemManager(Material.RED_BANNER).setDisplayName("Abbrechen").build(), () -> {
            InventoryMenuManager.getInstance().closeMenu(player, CloseReason.RETRUNPREVIOUS);
        }));
        content.addGuiItem(43, new InventoryItem(new ItemManager(Material.GREEN_BANNER).setDisplayName("Speichern").build(), () -> {

            new QuestManager().changeQuestAmounts(npc, quest, Integer.parseInt(content.get(28).getItem().getItemMeta().getLore().get(0)), Double.parseDouble(content.get(30).getItem().getItemMeta().getLore().get(0)), questPacket);
            Chat.sendSuccessMessage(main.PREFIX, me.oxolotel.utils.wrapped.player.Player.of(p), "§cQuest erfolgreich bearbeitet");
            InventoryMenuManager.getInstance().closeMenu(player, CloseReason.RETRUNPREVIOUS);
            InventoryMenuManager.getInstance().getOpenMenu(player).refresh();
        }));

        return content;
    }

    @Override
    public boolean isClickAllowed(Player player, int i) {
        return i == 41 || i == 28 || i == 30 || i == 43;
    }

    private void saveMenus(Player p) {
        MenuView a = InventoryMenuManager.getInstance().getOpenMenu(p);
        @SuppressWarnings("unchecked")
        LinkedList<TrippleWrapper<CustomMenu, InventoryContent, Task>> tempOpenMenus = (LinkedList<TrippleWrapper<CustomMenu, InventoryContent, Task>>) ReflectionUtils.accessField(MenuView.class, a, "openMenus", LinkedList.class);
        List<CustomMenu> t = tempOpenMenus.stream().map(TrippleWrapper::getValue1).toList();
        openMenus.put(p.getUniqueId(), t);
    }

    public static HashMap<UUID, List<CustomMenu>> getOpenMenus() {
        return openMenus;
    }
}