package edu.vic.hologramas;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;

public class HologramManager {
    private final Plugin plugin;
    private final Map<String, ArmorStand> hologramas;

    public HologramManager(Plugin plugin) {
        this.plugin = plugin;
        this.hologramas = new HashMap();
    };

    public ArmorStand createHologram(Location location, String texto, String id) {
        // 1. Verificar si ya existe un holograma con ese ID
        if (hologramas.containsKey(id)) {
            removeHologram(id);
        }
        // 2. Crear ArmorStand en la location
        ArmorStand holograma = (ArmorStand) location
                .getWorld()
                .spawnEntity(location, EntityType.ARMOR_STAND);

        // 3. Configurar ArmorStand:
        holograma.setVisible(false);
        holograma.setGravity(false);
        holograma.setCanPickupItems(false); // No recoge items
        holograma.setMarker(true); // No colisiona
        holograma.setCustomName(texto);
        holograma.setCustomNameVisible(true);
        holograma.setSmall(true);
        holograma.setArms(false);
        holograma.setBasePlate(false);

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
}
