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

public class Soldado extends JavaPlugin implements Listener {

    private NPC npc;
    private Location puntoA, puntoB;
    private boolean yendoAPuntoB = true;
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
        if (!(sender instanceof Player jugador)) return false;

        // Crear el NPC
        if (label.equalsIgnoreCase("crearnpc")) {
            // Si args tiene un valor, usarlo como nombre del skin
            String npcSkinName = NPC_SKINNAME;
            if (args.length > 0) npcSkinName= args[0];   
            return crearNPC(jugador, npcSkinName);
        }

        // Eliminar el NPC
        if (label.equalsIgnoreCase("eliminarnpcs")) {
            return eliminarNPCs(jugador);
        }

        // Iniciar patrullaje
        if (label.equalsIgnoreCase("iniciarpatrullaje")) {
            if (npc == null || !npc.isSpawned()) {
                jugador.sendMessage(ChatColor.RED + "No hay NPCs para patrullar.");
                return false;
            }
            iniciarPatrullaje();
            jugador.sendMessage(ChatColor.GREEN + "Patrullaje iniciado.");
            return true;
        }

        return false;
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
                skinTrait.setSkinName( skinName, true);
                skinTrait.setShouldUpdateSkins(true);
                getLogger().info("Skin configurada para: "+ NPC_NAME);
            } catch (Exception e) {
                getLogger().warning("Error configurando skin: " + e.getMessage());
            }

            jugador.sendMessage("NPC creado");

            // Establecer puntos A y B
            Location loc = jugador.getLocation();
            puntoA = loc.clone().add(loc.getDirection().multiply(2).setY(0));;
            puntoB = loc.clone().add(loc.getDirection().multiply(15).setY(0)); 
            // Spawn del NPC en el punto A
            npc.spawn(puntoA, SpawnReason.CREATE); 
            getLogger().info("NPC creado y spawneado en: " + puntoA.toString());
            return true;
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

        // Configurar navegación
        npc.setProtected(false);
        npc.getNavigator().getDefaultParameters().range(50).baseSpeed(1.5f);

        Location destino = yendoAPuntoB ? puntoB : puntoA;
        npc.getNavigator().setTarget(destino);
        getLogger().info("NPC moviéndose a: " + destino.toString());
    }

    // --- Manejador de eventos para la finalización de la navegación ---
    @EventHandler
    public void onNavigationComplete(NavigationCompleteEvent event) { 
        if (event.getNPC().equals(this.npc)) { // Asegúrate de que sea tu NPC
            getLogger().info("NPC llegó al destino, ahora cambia de dirección.");
            yendoAPuntoB = !yendoAPuntoB; // Cambia la dirección
            Location destino = yendoAPuntoB ? puntoB : puntoA;
            npc.getNavigator().setTarget(destino);
            getLogger().info("NPC moviéndose a: " + destino.toString());
        }
    }
}