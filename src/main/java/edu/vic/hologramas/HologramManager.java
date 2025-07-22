package edu.vic.hologramas;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class HologramManager {
    private final Plugin plugin;
    private final Map<String, ArmorStand> hologramas;

    public HologramManager(Plugin plugin) {
        this.plugin = plugin;
        this.hologramas = new HashMap<>();
    }

    public ArmorStand createHologram(Location location, String texto, String id) {
        // 1. Verificar si ya existe un holograma con ese ID
        if (hologramas.containsKey(id)) {
            removeHologram(id);
        }
        // 2. Crear ArmorStand en la location
        ArmorStand holograma = (ArmorStand) location.getWorld()
                .spawnEntity(location, EntityType.ARMOR_STAND);

        // 3. Configurar ArmorStand:
        holograma.setVisible(true);
        holograma.setGravity(false);
        holograma.setCanPickupItems(false); // No recoge items
        holograma.setMarker(false); // Permite clicks
        holograma.setCustomName(texto);
        holograma.setCustomNameVisible(true);
        holograma.setSmall(true);
        holograma.setArms(true);
        holograma.setBasePlate(true);

        // 4. Guardar en el Map con el ID
        hologramas.put(id, holograma);
        // 5. Retornar el ArmorStand
        return holograma;

    }

    public boolean removeHologram(String id) {
        ArmorStand holograma = hologramas.get(id);
        if (holograma != null) {
            holograma.remove();
            hologramas.remove(id);
            return true;
        }
        return false;
    }

    public boolean updateHologram(String id, String nuevoTexto) {
        ArmorStand holograma = hologramas.get(id);
        if (holograma != null) {
            holograma.setCustomName(nuevoTexto);
            return true;
        }
        return false;
    }

    public ArmorStand getHologram(String id) {
        return hologramas.get(id);
    }

    public boolean isHologram(ArmorStand armorStand) {
        return hologramas.containsValue(armorStand);
    }

    public void removeAllHolograms() {
        for (ArmorStand holograma : hologramas.values()) {
            holograma.remove();
        }
        hologramas.clear();
    }

    public String getHologramaId(ArmorStand holograma) {
        for (Map.Entry<String, ArmorStand> entry : hologramas.entrySet()) {
            if (entry.getValue().equals(holograma)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public Set<String> getAllHologramaIds() {
        return hologramas.keySet();
    }

    public ArmorStand createMenuOption(Location location, String texto, String id) {
        if (hologramas.containsKey(id)) {
            removeHologram(id);
        }
        ArmorStand menuOption = (ArmorStand) location.getWorld()
                .spawnEntity(location, EntityType.ARMOR_STAND);

        // Configurar ArmorStand para menú vertical
        menuOption.setVisible(false);
        menuOption.setGravity(false);
        menuOption.setCanPickupItems(false);
        menuOption.setMarker(false); // Permite clicks
        menuOption.setCustomName("§b► §f" + texto); // Con colores y símbolo
        menuOption.setCustomNameVisible(true);
        menuOption.setSmall(true);
        menuOption.setArms(false);
        menuOption.setBasePlate(false);

        hologramas.put(id, menuOption);

        return menuOption;
    }

    public boolean crearMenuVertical(Player player) {
        Location base = player.getLocation().clone().add(0, 2, 2); // Ajustar altura

        createMenuOption(base.clone(), "INICIAR JUEGO", "iniciar_juego");
        createMenuOption(base.clone().add(0, -0.8, 0), "VER PUNTUACIÓN", "ver_puntuacion");
        createMenuOption(base.clone().add(0, -1.6, 0), "TERMINAR JUEGO", "terminar_juego");
        createMenuOption(base.clone().add(0, -2.4, 0), "VER CRÉDITOS", "creditos");

        return true;
    }
}