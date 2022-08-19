package me.yl.questsystem.manager;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class ItemManager {

        private final ItemStack item;
        private final ItemMeta itemMeta;

        public ItemManager(Material material){
            item = new ItemStack(material);
            itemMeta = item.getItemMeta();

        }

        public ItemManager setDisplayName(String name){
            itemMeta.setDisplayName(name);
            return this;
        }

        public ItemManager setEnchant(Enchantment enchant, int level, boolean res){
            itemMeta.addEnchant(enchant, level, res);
            return this;
        }

        public ItemManager setLore(ArrayList<String> lore){
            itemMeta.setLore(lore);
            return this;
        }

    public ItemManager setLore(String... lore){
        itemMeta.setLore(Arrays.asList(lore));
        return this;
    }

        public ItemStack build(){
            item.setItemMeta(itemMeta);
            return item;
        }


}
