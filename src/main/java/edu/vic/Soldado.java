package edu.vic;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.EntityPoseTrait;
import net.citizensnpcs.trait.SkinTrait;
import net.citizensnpcs.trait.EntityPoseTrait.EntityPose;
import net.citizensnpcs.api.event.SpawnReason;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Tag;

import edu.vic.comandos.HologramCommands;
import edu.vic.comandos.InventarioMenuCommand;
import edu.vic.inventariomenus.InventarioMenuManager;
import edu.vic.menus.MenuListener;
import edu.vic.menus.MenuManager;
import edu.vic.menus.MenuVertical;
import edu.vic.cbr.AccionNPC;
import edu.vic.cbr.CasoInteraccion;

import net.md_5.bungee.api.ChatColor;

import net.citizensnpcs.trait.waypoint.Waypoints;
import net.citizensnpcs.trait.waypoint.LinearWaypointProvider;
import net.citizensnpcs.trait.waypoint.Waypoint;

public class Soldado extends JavaPlugin implements Listener {

    private static final float VELOCIDAD_NORMAL = 1.0f;
    private static final float VELOCIDAD_HUIR = 1.8f;

    private static final Set<Material> BLOQUES_ABEDUL = EnumSet.of(
            Material.BIRCH_LOG,
            Material.STRIPPED_BIRCH_LOG,
            Material.BIRCH_WOOD,
            Material.STRIPPED_BIRCH_WOOD);

    private List<Location> detectarWaypointsAbedul(Player player, int radioXZ, int radioY, int maxPuntos) {
        List<Location> encontrados = new ArrayList<>();

        Location base = player.getLocation();
        World world = player.getWorld();

        int bx = base.getBlockX();
        int by = base.getBlockY();
        int bz = base.getBlockZ();

        for (int dx = -radioXZ; dx <= radioXZ; dx++) {
            for (int dz = -radioXZ; dz <= radioXZ; dz++) {
                for (int dy = -radioY; dy <= radioY; dy++) {
                    Block b = world.getBlockAt(bx + dx, by + dy, bz + dz);
                    if (!BLOQUES_ABEDUL.contains(b.getType())) {
                        continue;
                    }
                    Location wp = b.getLocation().add(0.5, 1.0, 0.5);
                    encontrados.add(wp);
                }
            }
        }

        // Ordena los puntos de acuerdo a su distancia al jugador
        encontrados.sort(Comparator.comparingDouble(l -> l.distanceSquared(base)));

        // Control del número máximo de puntos
        if (encontrados.size() > maxPuntos) {
            encontrados = new ArrayList<>(encontrados.subList(0, maxPuntos));
        }

        return encontrados;
    }

    private boolean configurarWaypointsDesdeLista(List<Location> waypoints) {
        // Si no hay lista de puntos, retorna falso
        if (waypoints == null || waypoints.isEmpty()) {
            return false;
        }

        Waypoints waypointsTrait = npc.getOrAddTrait(Waypoints.class);
        waypointsTrait.setWaypointProvider("linear");
        LinearWaypointProvider provider = (LinearWaypointProvider) waypointsTrait.getCurrentProvider();

        provider.setCycle(true);
        provider.setCachePaths(true);

        puntoPartida = waypoints.get(0);

        for (Location loc : waypoints) {
            provider.addWaypoint(new Waypoint(loc));
        }

        return true;
    }

    private NPC npc;

    private double[][] puntos = null;
    Location puntoPartida = null;

    // Constantes con el Nombre del NPC Y SU SKIN por defecto
    // Estos valores pueden ser modificados en el archivo config.yml
    // o al crear el NPC con el comando /crearnpc <nombre_skin>
    private String NPC_Name = "Soldado";
    private String NPC_SkinName = "saku328";

    private MenuManager menuManager;

    @Override
    public void onEnable() {

        inicializarMemoriaCBR();

        this.saveDefaultConfig(); // Crea config.yml si no existe
        cargarConfiguracionNPC();
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info(" ---> Plugin Soldado activado correctamente");

        // Registrar el comando menu
        getCommand("menustart").setExecutor(new InventarioMenuCommand());
        // Inicializar menu manager
        InventarioMenuManager.init(this);

        // Registrar los comandos para manejar los hologramas
        getCommand("eliminartodoholograma").setExecutor(new HologramCommands());

        // Inicializar Holograma
        menuManager = new MenuManager();
        MenuListener menuListener = new MenuListener(menuManager);
        Bukkit.getPluginManager().registerEvents(menuListener, this);
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
            this.reloadConfig(); // Recargar desde disco
            cargarConfiguracionNPC();
            jugador.sendMessage(ChatColor.GREEN + "Configuración del NPC recargada.");
            return true;
        }

        // Eliminar las opciones de menu
        if (label.equalsIgnoreCase("eliminarhologramas")) {
            menuManager.removeAllMenuOptions();
            jugador.sendMessage("Todos los hologramas eliminados");
            return true;
        }

        // Crear menus verticales
        if (label.equalsIgnoreCase("crearmenuvertical")) {
            MenuVertical menuVertical = new MenuVertical(menuManager);
            menuVertical.crearMenuVertical(jugador.getLocation());
            jugador.sendMessage("Menú vertical creado.");
            return true;
        }

        // Muestra la memoria CBR del NPC
        if (label.equalsIgnoreCase("npcmemoria")) {
            jugador.sendMessage(ChatColor.GOLD + "Memoria CBR del NPC:");

            if (memoria.isEmpty()) {
                jugador.sendMessage(ChatColor.GRAY + "No hay casos almacenados todavía.");
                return true;
            }

            for (CasoInteraccion caso : memoria) {
                jugador.sendMessage(ChatColor.YELLOW + "- " + caso.toString());
            }

            return true;
        }

        return false;
    }

    // Usar BukkitRunnable para revisar si hay jugadores cerca
    // y saludarles si no se ha hecho recientemente
    private void iniciarSistemaInteraccion() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (npc == null || !npc.isSpawned()) {
                    cancel();
                    return;
                }

                Player jugadorCerca = encontrarJugadorCerca();
                if (jugadorCerca != null) {
                    npc.faceLocation(jugadorCerca.getLocation());
                    if (estaPatrullando) {
                        // Pausar patrullaje
                        Waypoints waypointsTrait = npc.getOrAddTrait(Waypoints.class);
                        LinearWaypointProvider provider = (LinearWaypointProvider) waypointsTrait.getCurrentProvider();
                        provider.setPaused(true);
                        estaPatrullando = false;
                        tiempoUltimoSaludo = System.currentTimeMillis();

                        // CBR: seleccionar y ejecutar acción
                        String contextoInicial = obtenerContextoJugador(jugadorCerca);

                        AccionNPC accion = seleccionarAccionCBR(contextoInicial);
                        ejecutarAccionNPC(jugadorCerca, accion);

                        Player jugadorRef = jugadorCerca;
                        AccionNPC accionRef = accion;
                        String contextoRef = contextoInicial;

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                guardarCaso(jugadorRef, contextoRef, accionRef);
                            }
                        }.runTaskLater(Soldado.this, 40L);
                    }
                } else {
                    reanudarPatrullaje();
                }
            }
        }.runTaskTimer(this, 0L, 20L); // Ejecutar cada segundo
    }

    private void equiparItemNPC(Material material) {
        if (npc == null || !npc.isSpawned())
            return;

        if (!(npc.getEntity() instanceof LivingEntity entidad))
            return;

        entidad.getEquipment().setItemInMainHand(new ItemStack(material));
    }

    private Player encontrarJugadorCerca() {
        Location npcLoc = npc.getEntity().getLocation();
        double distanciaDeteccion = 3.0;

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld().equals(npcLoc.getWorld())) {
                double distancia = player.getLocation().distance(npcLoc);
                if (distancia <= distanciaDeteccion) {
                    return player;
                }
            }
        }

        return null;
    }

    private String obtenerContextoJugador(Player jugador) {
        Material item = jugador.getInventory().getItemInMainHand().getType();
        getLogger().info("Item en mano: " + item.name());

        if (Tag.ITEMS_SWORDS.isTagged(item)) {
            return "ARMADO";
        }

        if (Tag.FLOWERS.isTagged(item)) {
            return "OFRECE_FLOR";
        }

        if (jugador.isSneaking()) {
            return "INCLINADO";
        }

        return "CERCA";
    }

    private int distanciaContexto(String actual, String almacenado) {
        if (actual.equals(almacenado)) {
            return 0;
        }

        if ((actual.equals("OFRECE_FLOR") && almacenado.equals("INCLINADO")) ||
                (actual.equals("INCLINADO") && almacenado.equals("OFRECE_FLOR"))) {
            return 1;
        }

        if ((actual.equals("OFRECE_FLOR") || actual.equals("INCLINADO")) &&
                almacenado.equals("CERCA")) {
            return 1;
        }

        if (actual.equals("ARMADO") && almacenado.equals("CERCA")) {
            return 1;
        }

        return 2;
    }

    private AccionNPC accionAleatoria() {
        AccionNPC[] valores = AccionNPC.values();
        return valores[random.nextInt(valores.length)];
    }

    private AccionNPC seleccionarAccionCBR(String contextoActual) {

        getLogger().info("[CBR] Contexto actual: " + contextoActual);
        getLogger().info("[CBR] Recuperando casos similares...");

        if (memoria.isEmpty()) {
            getLogger().info("[CBR] Memoria vacía. Exploración inicial.");
            return accionAleatoria();
        }

        if (random.nextDouble() < FACTOR_EXPLORACION) {
            getLogger().info("[CBR] Exploración: probando acción aleatoria.");
            return accionAleatoria();
        }

        Map<AccionNPC, Integer> puntuaciones = new HashMap<>();

        for (CasoInteraccion caso : memoria) {
            int distancia = distanciaContexto(contextoActual, caso.getContextoJugador());
            int valor = caso.getRecompensa() - distancia;

            puntuaciones.merge(caso.getAccionNPC(), valor, Integer::sum);

            getLogger().info("[CBR] Caso evaluado: " + caso
                    + " | distancia=" + distancia
                    + " | valor=" + valor);
        }

        if (puntuaciones.isEmpty()) {
            return accionAleatoria();
        }

        AccionNPC mejorAccion = puntuaciones.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();

        getLogger().info("[CBR] Reutilización: acción seleccionada = " + mejorAccion);

        return mejorAccion;
    }

    private void ejecutarAccionNPC(Player jugador, AccionNPC accion) {
        switch (accion) {
            case SALUDAR -> {
                jugador.sendMessage(NPC_Name + ": ¡Hola, " + jugador.getName() + "! Bienvenido.");
                equiparItemNPC(Material.AIR);
            }
            case MOSTRAR_FLOR -> {
                jugador.sendMessage(NPC_Name + ": *te ofrece una flor* 🌸");
                equiparItemNPC(Material.POPPY);
            }
            case MOSTRAR_ESPADA -> {
                jugador.sendMessage(NPC_Name + ": *desenvaina la espada* ¡Mantén la distancia!");
                equiparItemNPC(Material.IRON_SWORD);
            }
            case HUIR -> {
                jugador.sendMessage(NPC_Name + ": ¡Eek! *sale corriendo*");
                equiparItemNPC(Material.AIR);
                huirDelJugador(jugador);
            }
        }
    }

    private int evaluarReaccionJugador(Player jugador, AccionNPC accion) {
        Material item = jugador.getInventory().getItemInMainHand().getType();

        boolean ofreceFlor = Tag.FLOWERS.isTagged(item);
        boolean estaInclinado = jugador.isSneaking();
        boolean estaArmado = Tag.ITEMS_SWORDS.isTagged(item);

        if (estaArmado) {
            getLogger().info("[CBR] Reacción del jugador: AGRESIVA");

            if (accion == AccionNPC.HUIR || accion == AccionNPC.MOSTRAR_ESPADA) {
                return 1;
            }

            return -4;
        }

        if (ofreceFlor) {
            getLogger().info("[CBR] Reacción del jugador: OFRECE_FLOR");

            if (accion == AccionNPC.SALUDAR || accion == AccionNPC.MOSTRAR_FLOR) {
                return 4;
            }

            if (accion == AccionNPC.HUIR || accion == AccionNPC.MOSTRAR_ESPADA) {
                return -3;
            }
        }

        if (estaInclinado) {
            getLogger().info("[CBR] Reacción del jugador: INCLINACION_AMISTOSA");

            if (accion == AccionNPC.SALUDAR || accion == AccionNPC.MOSTRAR_FLOR) {
                return 2;
            }

            if (accion == AccionNPC.HUIR || accion == AccionNPC.MOSTRAR_ESPADA) {
                return -2;
            }
        }

        getLogger().info("[CBR] Reacción del jugador: NEUTRAL");
        return 0;
    }

    private void guardarCaso(Player jugador, String contextoInicial, AccionNPC accion) {
        int recompensa = evaluarReaccionJugador(jugador, accion);

        CasoInteraccion caso = new CasoInteraccion(contextoInicial, accion, recompensa);
        memoria.add(caso);

        getLogger().info("[CBR] Revisión: recompensa obtenida = " + recompensa);
        getLogger().info("[CBR] Retención: caso guardado = " + caso);
    }

    private void huirDelJugador(Player jugador) {
        if (npc == null || !npc.isSpawned())
            return;

        Location npcLoc = npc.getEntity().getLocation();
        Location playerLoc = jugador.getLocation();

        // Vector desde jugador hacia NPC (dirección de escape)
        org.bukkit.util.Vector direccion = npcLoc.toVector().subtract(playerLoc.toVector()).normalize();

        // Destino a 10 bloques
        Location destino = npcLoc.clone().add(direccion.multiply(10));

        getLogger().info("[NPC] Huyendo hacia: " + destino);

        npc.getNavigator().getDefaultParameters().speedModifier(VELOCIDAD_HUIR);
        npc.getNavigator().getDefaultParameters().distanceMargin(1.5);
        npc.faceLocation(destino);
        npc.getNavigator().setTarget(destino);

        new BukkitRunnable() {
            @Override
            public void run() {
                npc.getNavigator().getDefaultParameters().speedModifier(VELOCIDAD_NORMAL);
                npc.getNavigator().getDefaultParameters().distanceMargin(1.0);

                getLogger().info("[NPC] Velocidad restaurada a patrullaje normal.");
            }
        }.runTaskLater(Soldado.this, 80L); // 4 segundos
    }

    private boolean estaPatrullando = true;
    private long tiempoUltimoSaludo = 0;

    // Memoria del CBR
    private final List<CasoInteraccion> memoria = new ArrayList<>();

    // Método para inicializar la memoria
    private void inicializarMemoriaCBR() {
        if (!memoria.isEmpty())
            return;

        getLogger().info("[CBR] Inicializando memoria base...");

        memoria.add(new CasoInteraccion("ARMADO", AccionNPC.HUIR, 2));
        memoria.add(new CasoInteraccion("ARMADO", AccionNPC.MOSTRAR_ESPADA, 2));

        memoria.add(new CasoInteraccion("ARMADO", AccionNPC.SALUDAR, -2));
        memoria.add(new CasoInteraccion("ARMADO", AccionNPC.MOSTRAR_FLOR, -2));
    }

    private static final double FACTOR_EXPLORACION = 0.10;
    private static final Random random = new Random();

    private void reanudarPatrullaje() {
        if (!estaPatrullando) {
            // Espera un poco antes de saludar
            long tiempoEspera = 3000; // 3 segundos
            if (System.currentTimeMillis() - tiempoUltimoSaludo > tiempoEspera) {
                Waypoints waypointsTrait = npc.getOrAddTrait(Waypoints.class);
                LinearWaypointProvider provider = (LinearWaypointProvider) waypointsTrait.getCurrentProvider();
                provider.setPaused(false);

                estaPatrullando = true;
            }
        }
    }

    // Cargar configuración del NPC desde el archivo config.yml
    private void cargarConfiguracionNPC() {
        NPC_Name = getConfig().getString("npc.name", NPC_Name);
        NPC_SkinName = getConfig().getString("npc.skin", NPC_SkinName);

        getLogger().info(" -> Cargando configuración del NPC: " + NPC_Name + " con skin: " + NPC_SkinName);

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

        List<Location> waypointsDetectados = detectarWaypointsAbedul(jugador, 12, 4, 10);
        boolean configurado = configurarWaypointsDesdeLista(waypointsDetectados);

        if (configurado) {
            jugador.sendMessage(ChatColor.GREEN + "Waypoints detectados: " + waypointsDetectados.size());
        } else {
            jugador.sendMessage(ChatColor.YELLOW
                    + "No se detectaron leños de abedul cerca. Usando waypoints del archivo config.yml");
            configurarWaypoints(jugador);
        }

        iniciarSistemaInteraccion();
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