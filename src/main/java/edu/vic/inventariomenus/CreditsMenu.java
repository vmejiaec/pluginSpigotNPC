package edu.vic.inventariomenus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class CreditsMenu extends BaseInventarioMenu {

    public CreditsMenu(Plugin plugin) {
        super(plugin, "Menú de créditos", 9);
    }

    @Override
    protected void populate(Inventory menu, Player player) {
        ItemStack start = new ItemStack(Material.EMERALD);
        ItemMeta meta = start.getItemMeta();
        meta.setDisplayName("Volver al Inicio");
        start.setItemMeta(meta);
        menu.setItem(4, start);
    }

    @Override
    protected void handleClick(Player player, int slot) {
        if (slot == 4) {
            player.sendMessage("Volver al inicio del juego");
            InventarioMenuManager.openStartMenu(player);
        }
    }

}
