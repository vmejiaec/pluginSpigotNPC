package edu.vic.inventariomenus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class GameplayMenu extends BaseInventarioMenu {

    public GameplayMenu(Plugin plugin) {
        super(plugin, "menú de Game Play", 9);
    }

    @Override
    protected void populate(Inventory menu, Player player) {
        ItemStack gameover = new ItemStack(Material.EMERALD);
        ItemMeta metaGameOver = gameover.getItemMeta();
        metaGameOver.setDisplayName("Game Over");
        gameover.setItemMeta(metaGameOver);

        ItemStack start = new ItemStack(Material.DIAMOND);
        ItemMeta metaStart = gameover.getItemMeta();
        metaStart.setDisplayName("Start");
        start.setItemMeta(metaStart);

        menu.setItem(4, start);
        menu.setItem(6, gameover);
    }

    @Override
    protected void handleClick(Player player, int slot) {
        if (slot == 4) {
            player.sendMessage("Comenzando el juego");
            InventarioMenuManager.openStartMenu(player);
        }
        if (slot == 6) {
            player.sendMessage("Game Over!!");
            InventarioMenuManager.openGameOverMenu(player);
        }
    }

}
