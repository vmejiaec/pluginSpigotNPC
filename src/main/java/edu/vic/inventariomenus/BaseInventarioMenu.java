package edu.vic.inventariomenus;

import org.bukkit.event.Listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.inventory.Inventory;

public abstract class BaseInventarioMenu implements Listener {
    protected final Plugin plugin;
    protected final String title;
    protected final int size;

    public BaseInventarioMenu(Plugin plugin, String title, int size) {
        this.plugin = plugin;
        this.title = title;
        this.size = size;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void open(Player player) {
        Inventory menu = player.getServer().createInventory(
                null,
                size,
                title); // Usar Component.text()
        populate(menu, player);
        player.openInventory(menu);
    }

    protected abstract void populate(Inventory menu, Player player);

    protected abstract void handleClick(Player player, int slot);

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player))
            return;
        if (!event.getView().getTitle().equals(title))
            return;

        event.setCancelled(true);
        handleClick(player, event.getSlot());
    }
}
