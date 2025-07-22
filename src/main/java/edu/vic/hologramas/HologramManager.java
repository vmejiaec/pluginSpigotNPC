package edu.vic.hologramas;

import java.lang.management.PlatformLoggingMXBean;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Slime;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class HologramManager {
    private final Plugin plugin;
    private final Map<String, Slime> hologramas;

    public HologramManager(Plugin plugin) {
        this.plugin = plugin;
        this.hologramas = new HashMap();
    };

    public Slime createHologram(Location location, String texto, String id) {
        // 1. Verificar si ya existe un holograma con ese ID
        if (hologramas.containsKey(id)) {
            removeHologram(id);
        }
        // 2. Crear slime en la location
        Slime holograma = (Slime) location.getWorld()
                .spawnEntity(location, EntityType.SLIME);

        // 3. Configurar slime:
        holograma.setSize(1);
        holograma.setInvisible(true);
        holograma.setGravity(false);
        holograma.setCanPickupItems(false); // No recoge items
        holograma.setCustomName(texto);
        holograma.setCustomNameVisible(true);
        holograma.setSilent(true);
        holograma.setAI(false);

        // 4. Guardar en el Map con el ID
        hologramas.put(id, holograma);
        // 5. Retornar el slime
        return holograma;

    }

    public boolean removeHologram(String id) {
        Slime holograma = hologramas.get(id);
        if (holograma != null) {
            holograma.remove();
            hologramas.remove(id);
            return true;
        }
        return false;
    }

    public boolean updateHologram(String id, String nuevoTexto) {
        Slime holograma = hologramas.get(id);
        if (holograma != null) {
            holograma.setCustomName(nuevoTexto);
            return true;
        }
        return false;
    }

    public Slime getHologram(String id) {
        return hologramas.get(id);
    }

    public boolean isHologram(Slime slime) {
        return hologramas.containsValue(slime);
    }

    public void removeAllHolograms() {
        for (Slime holograma : hologramas.values()) {
            holograma.remove();
        }
        hologramas.clear();
    }

    public String getHologramaId(Slime holograma) {
        for (Map.Entry<String, Slime> entry : hologramas.entrySet()) {
            if (entry.getValue().equals(holograma)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public Set<String> getAllHologramaIds() {
        return hologramas.keySet();
    }

    public Slime createMenuOption(Location location, String texto, String id) {
        if (hologramas.containsKey(id)) {
            removeHologram(id);
        }
        Slime menuOption = (Slime) location.getWorld()
                .spawnEntity(location, EntityType.SLIME);

        menuOption.setSize(1); // Pequeño pero clickeable
        menuOption.setInvisible(true);
        menuOption.setCustomName("§b► §f" + texto); // Con colores y símbolo
        menuOption.setCustomNameVisible(true);
        menuOption.setSilent(true);
        menuOption.setAI(false);
        menuOption.setGravity(false);

        hologramas.put(id, menuOption);

        return menuOption;
    }

    public boolean crearMenuVertical(Player player) {
        Location base = player.getLocation().clone().add(2, 3, 0);

        Slime s1 = createMenuOption(base.clone(), "INICIAR JUEGO", "iniciar_juego");
        plugin.getLogger().info("Slime 1 creado: " + (s1 != null ? "OK" : "FALLÓ"));
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            plugin.getLogger().info("Verificación: Slime 1 existe: " + (s1 != null && s1.isValid()));
        }, 40L);

        createMenuOption(base.clone().add(0, -0.6, 0), "VER PUNTUACIÓN", "ver_puntuacion");
        createMenuOption(base.clone().add(0, -1.2, 0), "TERMINAR JUEGO", "terminar_juego");
        createMenuOption(base.clone().add(0, -1.8, 0), "VER CRÉDITOS", "creditos");

        return true;
    }
}