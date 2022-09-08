package me.yl.questsystem.quest.editMenu;

import me.oxolotel.utils.bukkit.menuManager.InventoryMenuManager;
import me.oxolotel.utils.bukkit.menuManager.menus.*;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryContent;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryItem;
import me.oxolotel.utils.wrapped.Chat;

import me.yl.questsystem.manager.ItemManager;
import me.yl.questsystem.npc.NPC;
import me.yl.questsystem.quest.Quest;
import me.yl.questsystem.quest.QuestManager;
import me.yl.questsystem.main;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class ReeditQuestMenu extends CustomMenu implements Closeable, SlotCondition, Modifyable, Submenu, CommandModifyable, Loggable{

    private boolean commandCheck;
    private final InventoryContent content;
    private Player p;
    private final NPC npc;
    private Quest quest;

    public ReeditQuestMenu(int size, NPC npc, Quest quest) {
        super(size);
        setTitle("Quest bearbeiten");
        content = new InventoryContent();
        this.npc = npc;
        this.quest = quest;
    }

    @Override
    public void onClose(Player player, ItemStack[] itemStacks, CloseReason closeReason) {

    }

    @Override
    public InventoryContent getContents(Player player) {
        p = player;

        content.fill(0,54, new InventoryItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), ()->{}));
        content.addGuiItem(13, ()->{},Material.BLUE_DYE,"§9§lBearbeiten");
        content.addGuiItem(28, new InventoryItem(new ItemManager(Material.OAK_SIGN).setDisplayName("Item Menge").setLore(String.valueOf(quest.getItemAmount())).build(), ()->{
            commandCheck = false;
            awaitCommand(player);
        }));
        content.addGuiItem(30, new InventoryItem(new ItemManager(Material.OAK_SIGN).setDisplayName("Item Preis").setLore(String.valueOf(quest.getReward())).build(), ()->{
            commandCheck = true;
            awaitCommand(player);
        }));
        content.addGuiItem(37, new InventoryItem(quest.getItem(), ()->{}));
        content.addGuiItem(38, new InventoryItem(new ItemManager(Material.ARROW).build(), ()->{}));
        content.addGuiItem(39, new InventoryItem(new ItemManager(Material.GOLD_INGOT).setDisplayName(" ").build(), ()->{}));
        content.addGuiItem(41, new InventoryItem(new ItemManager(Material.RED_BANNER).setDisplayName("Abbrechen").build(), ()->{
            InventoryMenuManager.getInstance().closeMenu(player, CloseReason.RETRUNPREVIOUS);
        }));
        content.addGuiItem(43, new InventoryItem(new ItemManager(Material.GREEN_BANNER).setDisplayName("Speichern").build(), ()->{
            new QuestManager().changeQuestAmounts(npc, quest, Integer.parseInt(content.get(28).getItem().getItemMeta().getLore().get(0)), Double.parseDouble(content.get(30).getItem().getItemMeta().getLore().get(0)));
            Chat.sendSuccessMessage(main.PREFIX, me.oxolotel.utils.wrapped.player.Player.of(p),"§cQuest erfolgreich bearbeitet");
            InventoryMenuManager.getInstance().closeMenu(player, CloseReason.RETRUNPREVIOUS);
            InventoryMenuManager.getInstance().getOpenMenu(player).refresh();
        }));

        return content;
    }

    @Override
    public boolean isClickAllowed(Player player, int i) {
        return i == 41 || i == 28 || i == 30 || i == 43;
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
}
