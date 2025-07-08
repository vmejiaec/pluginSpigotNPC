package edu.vic;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.ai.event.NavigationCompleteEvent; // ¡La importación correcta!

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;

public class Soldado extends JavaPlugin implements Listener {

    private NPC npc;
    private Location puntoA, puntoB;
    private boolean yendoAPuntoB = true;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("Plugin Soldado activado correctamente");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player jugador)) return false;

        // Crear el NPC
        if (label.equalsIgnoreCase("crearnpc")) {
            if (npc != null && npc.isSpawned()) {
                jugador.sendMessage("El NPC ya está creado.");
                return true;
            }

            npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Aurora");

            Location loc = jugador.getLocation();
            npc.spawn(loc);

            // Configurar navegación
            npc.setProtected(false);
            npc.getNavigator().getDefaultParameters().range(50).baseSpeed(1.5f);

            jugador.sendMessage("NPC creado");

            // Establecer puntos A y B
            puntoA = loc;
            // Clona la ubicación inicial y añade una distancia en la dirección donde mira el jugador
            // para asegurar que puntoB sea diferente a puntoA.
            puntoB = loc.clone().add(loc.getDirection().multiply(12).setY(0)); 
            
            // Iniciar el primer movimiento del patrullaje
            iniciarPatrullaje(); 
            return true;
        }

        // Eliminar el NPC
        if (label.equalsIgnoreCase("eliminarnpcs")) {
            return eliminarNPCs(jugador);
        }

        return false;
    }

    // Eliminar todos los NPCs
    private boolean eliminarNPCs(Player jugador){
        int cantidad = 0;
        for (NPC npc : CitizensAPI.getNPCRegistry()) {
            npc.destroy();
            cantidad++;
        }
        jugador.sendMessage(ChatColor.RED + "Eliminados " + cantidad + " NPC(s).");
        return true;
    }

    // Este método inicia el movimiento al siguiente punto de patrullaje
    private void iniciarPatrullaje() {
        if (npc == null || !npc.isSpawned()) return;

        Location destino = yendoAPuntoB ? puntoB : puntoA;
        npc.getNavigator().setTarget(destino);
        getLogger().info("NPC moviéndose a: " + destino.toString());
    }

    // --- Manejador de eventos para la finalización de la navegación ---
    @EventHandler
    public void onNavigationComplete(NavigationCompleteEvent event) { // ¡Nombre del evento y tipo corregidos!
        if (event.getNPC().equals(this.npc)) { // Asegúrate de que sea tu NPC
            getLogger().info("NPC llegó al destino, ahora cambia de dirección.");
            yendoAPuntoB = !yendoAPuntoB; // Cambia la dirección
            iniciarPatrullaje(); // Inicia el movimiento al nuevo destino
        }
    }

    @EventHandler
    public void alEntrar(PlayerJoinEvent event) {
        Player jugador = event.getPlayer();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (npc == null || !npc.isSpawned()) {
                    cancel(); // Cancelar la tarea si el NPC no existe o no está spawn
                    return;
                }

                Location npcLoc = npc.getEntity().getLocation();
                Location playerLoc = jugador.getLocation();

                if (npcLoc.getWorld().equals(playerLoc.getWorld())
                        && npcLoc.distance(playerLoc) < 5.0
                        && jugador.getInventory().getItemInMainHand().getType().name().contains("SWORD")) {

                    String tipoEspada = jugador.getInventory().getItemInMainHand().getType().name();
                    jugador.sendMessage("Aurora: ¿Es eso una " + tipoEspada.toLowerCase().replace("_", " ") + "?");
                }
            }
        }.runTaskTimer(this, 60L, 60L); // cada 3 segundos, desde los 3 segundos
    }
}