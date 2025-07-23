package edu.vic.menus;

import org.bukkit.Location;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class MenuListener implements Listener {
    private final MenuManager manager;

    public MenuListener(MenuManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        if (!(event.getRightClicked() instanceof Interaction menuOptionInter)) {
            return;
        }

        if (!manager.isMenuOptionInter(menuOptionInter)) {
            return;
        }

        Player player = event.getPlayer();
        String menuOptionId = manager.getMenuOptionInterId(menuOptionInter);

        event.setCancelled(true);

        handleClick(player, menuOptionId);
    }

    private void handleClick(Player player, String id) {
        // Reproducir sonido y partículas al dar click
        MenuPair menuOption = manager.getMenuOption(id);
        if (menuOption != null) {
            playClickEffect(player, menuOption.getLocation());
        }
        // Gestión del clik en su respectivo holograma
        switch (id) {
            case "iniciar_juego":
                player.sendMessage("§a🎮 Iniciando el juego - H.");
                break;
            case "ver_puntuación":
                player.sendMessage("§e📊 Mostrando puntuación");
                break;
            case "terminar_juego":
                player.sendMessage("§c🛑 Terminando juego");
                break;
            case "creditos":
                player.sendMessage("§b💠 Mostrando los créditos");
                break;
            default:
                player.sendMessage("Opción de menú clickeada: " + id);
                break;
        }
    }

    private void playClickEffect(Player player, Location loc) {
        player.getWorld().playSound(
                loc,
                org.bukkit.Sound.UI_BUTTON_CLICK,
                1.0f, 1.0f);
        player.spawnParticle(
                org.bukkit.Particle.VILLAGER_HAPPY,
                loc.clone().add(0, 0.5, 0),
                5, 0.2, 0.2, 0.2, 0);
    }
}
