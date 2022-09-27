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
import me.yl.questsystem.manager.*;
import me.yl.questsystem.npc.NPC;
import me.yl.questsystem.quest.Quest;
import me.yl.questsystem.quest.QuestConfigManager;
import me.yl.questsystem.quest.QuestManager;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class CreateQuestMenu extends CustomMenu implements Closeable, SlotCondition, Modifyable, Submenu, Loggable {

    private final InventoryContent content;
    private Player p;
    private final NPC npc;
    private static HashMap<UUID, List<CustomMenu>> openMenus = new HashMap<>();
    private int questPacket;

    public CreateQuestMenu(int size, NPC npc, Player p, int nr) {
        super(size);
        setTitle("Create Quest");
        content = new InventoryContent();
        this.npc = npc;
        questPacket = nr;
    }


    @Override
    public void onClose(Player player, ItemStack[] itemStacks, CloseReason closeReason) {
        if (closeReason != CloseReason.CHANGEMENU) {
            openMenus.remove(p.getUniqueId());
            new ProtocolLibReader().removeMengeInput(player);
            new ProtocolLibReader().removePreisInput(player);
            new MenuClick().removePlayer(p);
        }
    }

    @Override
    public InventoryContent getContents(Player player) {
        p = player;
        content.fill(0,54, new InventoryItem(new ItemManager(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").build(), ()->{}));
        content.addGuiItem(13, new InventoryItem(new ItemManager(Material.DIAMOND_PICKAXE).setDisplayName("Quest erstellen").build(), ()->{}));
        if (new MenuClick().getItem(player) != null){
            content.addGuiItem(37, new InventoryItem(new ItemManager(new MenuClick().getItem(player).getType()).setDisplayName(" ").build(), null));
        }else{
            content.addGuiItem(37, null);
        }



        content.addGuiItem(28, new InventoryItem(new ItemManager(Material.OAK_SIGN).setDisplayName("Item Menge").build(), ()->{
            ItemStack item = null;
            if (new MenuClick().getItem(player) == null) {
                item = new ItemManager(Material.BARRIER).setDisplayName(" ").build();
            }else{
                item = new ItemManager(new MenuClick().getItem(player).getType()).setDisplayName(" ").build();
            }
            saveMenus(player);
            InventoryMenuManager.getInstance().closeMenu(player, Closeable.CloseReason.CHANGEMENU);
            new AnvilMenuManager().createAnvilMenu(player, item, "Create Quest Menge");
        }));
        content.addGuiItem(30, new InventoryItem(new ItemManager(Material.OAK_SIGN).setDisplayName("Item Preis").build(), ()->{
            ItemStack item = new ItemManager(Material.GOLD_INGOT).setDisplayName(" ").build();
            saveMenus(player);
            InventoryMenuManager.getInstance().closeMenu(player, Closeable.CloseReason.CHANGEMENU);
            new AnvilMenuManager().createAnvilMenu(player, item, "Create Quest Preis");
        }));
        if (getOpenMenus().containsKey(player.getUniqueId())) {
            ProtocolLibReader plr = new ProtocolLibReader();
            if (plr.getMengeInput().containsKey(player.getUniqueId())) {
                content.addGuiItem(28, new InventoryItem(new ItemManager(Material.OAK_SIGN).setDisplayName("Item Menge").setLore(plr.getMengeInput().get(player.getUniqueId())).build(), () -> {
                    ItemStack item = null;
                    if (new MenuClick().getItem(player) == null) {
                        item = new ItemManager(Material.BARRIER).setDisplayName(" ").build();
                    }else{
                        item = new ItemManager(new MenuClick().getItem(player).getType()).setDisplayName(" ").build();
                    }
                    saveMenus(player);
                    InventoryMenuManager.getInstance().closeMenu(player, Closeable.CloseReason.CHANGEMENU);
                    new AnvilMenuManager().createAnvilMenu(player, item, "Create Quest Menge");
                }));
            }
            if (plr.getPreisInput().containsKey(player.getUniqueId())) {
                content.addGuiItem(30, new InventoryItem(new ItemManager(Material.OAK_SIGN).setDisplayName("Item Preis").setLore(plr.getPreisInput().get(player.getUniqueId())).build(), () -> {
                    ItemStack item = new ItemManager(Material.GOLD_INGOT).setDisplayName(" ").build();
                    saveMenus(player);
                    InventoryMenuManager.getInstance().closeMenu(player, Closeable.CloseReason.CHANGEMENU);
                    new AnvilMenuManager().createAnvilMenu(player, item, "Create Quest Preis");
                }));
            }
        }

        content.addGuiItem(38, new InventoryItem(new SkullManager("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifX19",
                " ").build(), ()->{}));
        content.addGuiItem(39, new InventoryItem(new ItemManager(Material.GOLD_INGOT).setDisplayName(" ").build(), ()->{}));
        content.addGuiItem(41, new InventoryItem(new ItemManager(Material.RED_BANNER).setDisplayName("Abbrechen").build(), ()->{
            InventoryMenuManager.getInstance().closeMenu(player, CloseReason.RETRUNPREVIOUS);
        }));
        content.addGuiItem(43, new InventoryItem(new ItemManager(Material.GREEN_BANNER).setDisplayName("Speichern").build(), ()->{
            if(checkQuestInput()){
                int menge = Integer.parseInt(content.get(28).getItem().getItemMeta().getLore().get(0));
                double preis = Double.parseDouble(content.get(30).getItem().getItemMeta().getLore().get(0));
                Quest q = null;

                q = new Quest(content.get(37).getItem(), menge, preis, npc);

                new QuestConfigManager().writeQuesttConfig(q, questPacket);
                InventoryMenuManager.getInstance().closeMenu(player, CloseReason.RETRUNPREVIOUS);
                InventoryMenuManager.getInstance().getOpenMenu(player).refresh();
            }
        }));

        return content;
    }

    @Override
    public boolean isClickAllowed(Player player, int i) {
        if (i == 37 || i == 41 || i == 30 || i == 43 || i == 28){
            return true;
        }
        return false;
    }

    private boolean checkQuestInput(){
        if (new MenuClick().getItem(p) != null) {
            content.addGuiItem(37, new InventoryItem(new MenuClick().getItem(p), () -> {}));

            if (content.get(37) != null || content.get(37).getItem().getType() != Material.AIR) {
                return content.get(28).getItem().getItemMeta().hasLore() && content.get(30).getItem().getItemMeta().hasLore();
            }
        }
        return false;
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