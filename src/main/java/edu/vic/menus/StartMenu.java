package edu.vic.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class StartMenu extends BaseMenu {

    public StartMenu(Plugin plugin) {
        super(plugin, "Inicio del juego", 9);
    }

    @Override
    protected void populate(Inventory menu, Player player) {
        ItemStack start = new ItemStack(Material.EMERALD);
        ItemMeta meta = start.getItemMeta();
        meta.setDisplayName("Comenzar");
        start.setItemMeta(meta);
        menu.setItem(4, start);
    }

    @Override
    protected void handleClick(Player player, int slot) {
        if (slot == 4) {
            player.sendMessage("Comenzando el juego");
            MenuManager.openGameplayMenu(player);
        }
    }

}
