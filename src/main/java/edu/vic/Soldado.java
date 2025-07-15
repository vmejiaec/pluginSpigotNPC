package edu.vic;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;
import net.citizensnpcs.api.ai.event.NavigationCompleteEvent;
import net.citizensnpcs.api.event.SpawnReason;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.ChatColor;

import net.citizensnpcs.trait.waypoint.Waypoints;
import net.citizensnpcs.trait.waypoint.LinearWaypointProvider;
import net.citizensnpcs.trait.waypoint.Waypoint;

public class Soldado extends JavaPlugin implements Listener {

    private NPC npc;
    private Location[] puntos = new Location[4];
    // ConstanteS con el Nombre del NPC Y SU SKIN
    private static final String NPC_NAME = "Soldado"; // Nombre del NPC
    private static final String NPC_SKINNAME = "saku328"; // Int3ns1ve - DemosthenesV2 - StinkerToo . saku328

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("Plugin Soldado activado correctamente");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player jugador))
            return false;

        // Crear el NPC
        if (label.equalsIgnoreCase("crearnpc")) {
            // Si args tiene un valor, usarlo como nombre del skin
            String npcSkinName = NPC_SKINNAME;
            if (args.length > 0)
                npcSkinName = args[0];
            return crearNPC(jugador, npcSkinName);
        }

        // Eliminar el NPC
        if (label.equalsIgnoreCase("eliminarnpcs")) {
            return eliminarNPCs(jugador);
        }

        return false;
    }

    // Configurar waypoints
    private void configurarWaypoints(Player jugador) {
        // 1. Obtener el trait de waypoints
        Waypoints waypointsTrait = npc.getOrAddTrait(Waypoints.class);

        // 2. Establecer el proveedor linear
        waypointsTrait.setWaypointProvider("linear");

        // 3. Obtener el proveedor linear
        LinearWaypointProvider provider = (LinearWaypointProvider) waypointsTrait.getCurrentProvider();

        // 4. Configurar el provider
        provider.setCycle(true); // Repetir la ruta en bucle
        provider.setCachePaths(true); // Cachear paths para mejor rendimiento

        // Establecer puntos A y B
        Location loc = jugador.getLocation();
        loc = loc.clone();
        loc.setX(-41);
        loc.setY(63);
        loc.setZ(46);
        puntos[0] = loc;

        loc = loc.clone();
        loc.setX(-49);
        loc.setY(63);
        loc.setZ(46);
        puntos[1] = loc;

        loc = loc.clone();
        loc.setX(-49);
        loc.setY(63);
        loc.setZ(40);
        puntos[2] = loc;

        loc = loc.clone();
        loc.setX(-53);
        loc.setY(63);
        loc.setZ(36);
        puntos[3] = loc;

        loc = loc.clone();
        loc.setX(-41);
        loc.setY(63);
        loc.setZ(36);
        puntos[4] = loc;

        // 5. Crear y añadir waypoints
        for (Location punto : puntos) {
            Waypoint waypoint = new Waypoint(punto);
            provider.addWaypoint(waypoint);
        }
    }

    // Crear el NPC con skin y nombre
    private boolean crearNPC(Player jugador, String skinName) {
        if (npc != null && npc.isSpawned()) {
            jugador.sendMessage("El NPC ya está creado.");
            return true;
        }

        // Crear el NPC con su nombre
        npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, NPC_NAME);
        // Configurar skin del NPC
        try {
            SkinTrait skinTrait = npc.getOrAddTrait(SkinTrait.class);
            skinTrait.setSkinName(skinName, true);
            skinTrait.setShouldUpdateSkins(true);
            getLogger().info("Skin configurada para: " + NPC_NAME);
        } catch (Exception e) {
            getLogger().warning("Error configurando skin: " + e.getMessage());
        }
        jugador.sendMessage("NPC creado");

        configurarWaypoints(jugador);

        // Spawn del NPC en el punto A
        npc.spawn(puntoA, SpawnReason.CREATE);
        getLogger().info("NPC creado y spawneado en: " + puntoA.toString());
        return true;
    }

    // Eliminar todos los NPCs
    private boolean eliminarNPCs(Player jugador) {
        int cantidad = 0;
        for (NPC npc : CitizensAPI.getNPCRegistry()) {
            npc.destroy();
            cantidad++;
        }
        jugador.sendMessage(ChatColor.RED + "Eliminados " + cantidad + " NPC(s).");
        return true;
    }

}