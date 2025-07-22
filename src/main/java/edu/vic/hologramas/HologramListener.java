package edu.vic.hologramas;

import org.bukkit.entity.Slime;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.plugin.Plugin;

public class HologramListener implements Listener {

    private final Plugin plugin;
    private final HologramManager hologramManager;

    public HologramListener(Plugin plugin, HologramManager hologramManager) {
        this.plugin = plugin;
        this.hologramManager = hologramManager;
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        if (!(event.getRightClicked() instanceof Slime slime)) {
            return;
        }

        if (!hologramManager.isHologram(slime)) {
            return;
        }

        Player player = event.getPlayer();
        String hologramId = hologramManager.getHologramaId(slime);

        event.setCancelled(true);

        handleHologramClick(player, hologramId);
    }

    private void handleHologramClick(Player player, String hologramId) {
        // Reproducir sonido y partículas al dar click
        Slime holograma = hologramManager.getHologram(hologramId);
        if (holograma != null) {
            playClickEffect(player, holograma);
        }
        // Gestión del clik en su respectivo holograma
        switch (hologramId) {
            case "iniciar_juego":
                player.sendMessage("Iniciando el juego - H.");
                break;
            case "ver_puntuación":
                player.sendMessage("Mostrando puntuación");
                break;
            case "terminar_juego":
                player.sendMessage("Terminando juego");
                break;
            case "creditos":
                player.sendMessage("Mostrando los créditos");
                break;
            default:
                player.sendMessage("Holograma clickeado: " + hologramId);
                break;
        }
    }

    private void playClickEffect(Player player, Slime holograma) {
        player.getWorld().playSound(
                holograma.getLocation(),
                org.bukkit.Sound.UI_BUTTON_CLICK,
                1.0f, 1.0f);
        player.spawnParticle(
                org.bukkit.Particle.VILLAGER_HAPPY,
                holograma.getLocation().add(0, 0.5, 0),
                5, 0.2, 0.2, 0.2, 0);
    }
}
