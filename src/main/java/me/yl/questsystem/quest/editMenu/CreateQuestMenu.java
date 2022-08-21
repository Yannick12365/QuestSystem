package me.yl.questsystem.quest.editMenu;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.oxolotel.utils.bukkit.menuManager.InventoryMenuManager;
import me.oxolotel.utils.bukkit.menuManager.menus.*;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryContent;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryItem;
import me.oxolotel.utils.wrapped.Chat;
import me.yl.questsystem.listener.MenuClick;
import me.yl.questsystem.manager.ItemManager;
import me.yl.questsystem.manager.SkullManager;
import me.yl.questsystem.npc.NPC;
import me.yl.questsystem.quest.Quest;
import me.yl.questsystem.quest.QuestConfigManager;
import me.yl.questsystem.quest.QuestManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.UUID;


public class CreateQuestMenu extends CustomMenu implements Closeable, SlotCondition, Modifyable, Submenu, CommandModifyable, Loggable {

    private boolean commandCheck;
    private final InventoryContent content;
    private Player p;
    private final NPC npc;

    public CreateQuestMenu(int size, NPC npc) {
        super(size);
        setTitle("Create Quest");
        content = new InventoryContent();
        this.npc = npc;
    }

    @Override
    public void onClose(Player player, ItemStack[] itemStacks, CloseReason closeReason) {

    }

    @Override
    public InventoryContent getContents(Player player) {
        p = player;
        content.fill(0,54, new InventoryItem(new ItemManager(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").build(), ()->{}));
        content.addGuiItem(13, new InventoryItem(new ItemManager(Material.DIAMOND_PICKAXE).setDisplayName("Quest erstellen").build(), ()->{}));
        content.addGuiItem(28, new InventoryItem(new ItemManager(Material.OAK_SIGN).setDisplayName("Item Menge").build(), ()->{
            commandCheck = false;
            awaitCommand(player);
        }));
        content.addGuiItem(30, new InventoryItem(new ItemManager(Material.OAK_SIGN).setDisplayName("Item Preis").build(), ()->{
            commandCheck = true;
            awaitCommand(player);
        }));
        content.addGuiItem(37, null);

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
                Quest q;
                if (new QuestManager().getQuestList().get(npc) == null || new QuestManager().getQuestList().get(npc).size() == 0){
                    q = new Quest(content.get(37).getItem(), menge, preis, npc, 1, true);
                }else {
                    q = new Quest(content.get(37).getItem(), menge, preis, npc);
                }
                    new QuestConfigManager().writeQuesttConfig(q);
                    InventoryMenuManager.getInstance().closeMenu(player, CloseReason.RETRUNPREVIOUS);
                    InventoryMenuManager.getInstance().getOpenMenu(player).refresh();
            }
        }));

        return content;
    }

    @Override
    public boolean isClickAllowed(Player player, int i) {
        return i == 37 || i == 41 || i == 28 || i == 30 || i == 43;
    }

    @Override
    public String getCommand() {
        if (commandCheck){
            return "QuestPreis";
        }else{
            return "QuestMenge";
        }
    }

    @Override
    public String getCommandHelp() {
        if (commandCheck){
           return  "Gib /QuestPreis <Preis> ein um den Preis festzulegen";
        }else{
            return  "Gib /QuestMenge <Menge> ein um die Menge festzulegen";
        }
    }

    @Override
    public void processCommand(String[] strings) {
        if (commandCheck){
            if (new MenuClick().getItemMap().containsKey(p.getUniqueId())){
                content.addGuiItem(37,new InventoryItem(new MenuClick().getItem(p),()->{}));
                new MenuClick().removePlayer(p);
            }
            if (new QuestManager().checkSignLorePreis(strings[0])) {
                strings[0] = strings[0].replace(",", ".");
                content.addGuiItem(30, new InventoryItem(new ItemManager(Material.OAK_SIGN).setDisplayName("Item Preis").setLore(strings).build(), () -> {
                    commandCheck = true;
                    awaitCommand(p);
                }));
            }else {
                Chat.sendErrorMessage("Questsystem", me.oxolotel.utils.wrapped.player.Player.of(p),
                        "Yo Du was gibst du da ein bist du dumm das ist doch kein Preis?");
            }
        }else{
            if (new MenuClick().getItemMap().containsKey(p.getUniqueId())){
                content.addGuiItem(37,new InventoryItem(new MenuClick().getItem(p),()->{}));
                new MenuClick().removePlayer(p);
            }
            if (new QuestManager().checkSignLoreMenge(strings[0])) {
                content.addGuiItem(28, new InventoryItem(new ItemManager(Material.OAK_SIGN).setDisplayName("Item Menge").setLore(strings).build(), () -> {
                    commandCheck = false;
                    awaitCommand(p);
                }));
            }else {
                Chat.sendErrorMessage("Questsystem", me.oxolotel.utils.wrapped.player.Player.of(p),
                        "Yo Du was gibst du da ein bist du dumm das ist doch kein Menge?");
            }
        }
    }

    public boolean checkQuestInput(){
        if(content.get(37) != null || content.get(37).getItem().getType() != Material.AIR){
            return content.get(28).getItem().getItemMeta().hasLore() && content.get(30).getItem().getItemMeta().hasLore();
        }
        return false;




    }


    public ItemStack getCustomSkull(String base64) {

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        if (base64.isEmpty()) return head;

        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        profile.getProperties().put("textures", new Property("textures", base64));

        try {
            Method mtd = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
            mtd.setAccessible(true);
            mtd.invoke(skullMeta, profile);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            ex.printStackTrace();
        }

        head.setItemMeta(skullMeta);
        return head;
    }

}