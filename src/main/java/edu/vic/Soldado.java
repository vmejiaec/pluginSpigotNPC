package edu.vic;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;
import net.citizensnpcs.api.ai.event.NavigationCompleteEvent;
import net.citizensnpcs.api.event.SpawnReason;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.ChatColor;

import net.citizensnpcs.trait.waypoint.Waypoints;
import net.citizensnpcs.trait.waypoint.LinearWaypointProvider;
import net.citizensnpcs.trait.waypoint.Waypoint;

public class Soldado extends JavaPlugin implements Listener {

    private NPC npc;

    private double[][] puntos = null;
    Location puntoPartida = null;

    // Constantes con el Nombre del NPC Y SU SKIN por defecto
    // Estos valores pueden ser modificados en el archivo config.yml
    // o al crear el NPC con el comando /crearnpc <nombre_skin>
    private String NPC_Name = "Soldado";
    private String NPC_SkinName = "saku328";

    @Override
    public void onEnable() {
        this.saveDefaultConfig(); // Crea config.yml si no existe
        cargarConfiguracionNPC();
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
            if (args.length > 0)
                NPC_SkinName = args[0];
            return crearNPC(jugador, NPC_SkinName);
        }

        // Eliminar el NPC
        if (label.equalsIgnoreCase("eliminarnpcs")) {
            return eliminarNPCs(jugador);
        }

        // Volver a cargar la configuración del NPC
        if (label.equalsIgnoreCase("recargarconfig")) {
            cargarConfiguracionNPC();
            jugador.sendMessage(ChatColor.GREEN + "Configuración del NPC recargada.");
            return true;
        }

        return false;
    }

    // Cargar configuración del NPC desde el archivo config.yml
    private void cargarConfiguracionNPC() {
        NPC_Name = getConfig().getString("npc.name", NPC_Name);
        NPC_SkinName = getConfig().getString("npc.skin", NPC_SkinName);

        List<String> waypointsList = getConfig().getStringList("waypoints");
        puntos = new double[waypointsList.size()][3];

        for (int i = 0; i < waypointsList.size(); i++) {
            String[] coords = waypointsList.get(i).split(",");
            puntos[i][0] = Double.parseDouble(coords[0]);
            puntos[i][1] = Double.parseDouble(coords[1]);
            puntos[i][2] = Double.parseDouble(coords[2]);
        }

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
        provider.setCycle(true);
        provider.setCachePaths(true);

        // Establecer puntos
        World world = jugador.getWorld();
        puntoPartida = new Location(world, puntos[0][0], puntos[0][1], puntos[0][2]);

        // 5. Crear y añadir waypoints
        for (var punto : puntos) {
            Location loc = new Location(world, punto[0], punto[1], punto[2]);
            Waypoint waypoint = new Waypoint(loc);
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
        npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, NPC_Name);
        // Configurar skin del NPC
        try {
            SkinTrait skinTrait = npc.getOrAddTrait(SkinTrait.class);
            skinTrait.setSkinName(skinName, true);
            skinTrait.setShouldUpdateSkins(true);
            getLogger().info("Skin configurada para: " + NPC_Name + " con skin: " + skinName);
        } catch (Exception e) {
            getLogger().warning("Error configurando skin: " + e.getMessage());
        }
        jugador.sendMessage("NPC creado");

        configurarWaypoints(jugador);

        // Spawn del NPC en el primer punto
        npc.spawn(puntoPartida, SpawnReason.CREATE);
        getLogger().info("NPC creado y spawneado en: " + puntoPartida.toString());
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