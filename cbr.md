# Guía de implementación CBR

Todos los cambios se hacen en **un único archivo**: `src/main/java/edu/vic/Soldado.java`.  
Los archivos `CasoInteraccion.java` y `AccionNPC.java` ya están creados y correctos, no los toques.

---

## 1. Imports — añádir al bloque de imports de `Soldado.java`

Busca la línea `import java.util.Set;` y **debajo** añade estas tres líneas:

```java
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
```

Busca la línea `import edu.vic.menus.MenuVertical;` y **debajo** añade:

```java
import edu.vic.cbr.AccionNPC;
import edu.vic.cbr.CasoInteraccion;
import org.bukkit.Tag;
```

---

## 2. Campo `memoria` — añadir en la clase `Soldado`

Busca estas dos líneas que ya existen:

```java
private boolean estaPatrullando = true;
private long tiempoUltimoSaludo = 0;
```

**Debajo** de ellas añade:

```java
private final List<CasoInteraccion> memoria = new ArrayList<>();
private static final double FACTOR_EXPLORACION = 0.25;
private static final Random random = new Random();
```

---

## 3. Método `accionAleatoria()` — nuevo método privado

Añádelo **al final de la clase**, antes del `}` de cierre:

```java
private AccionNPC accionAleatoria() {
    AccionNPC[] valores = AccionNPC.values();
    return valores[random.nextInt(valores.length)];
}
```

---

## 4. Método `seleccionarAccionCBR(Player jugador)` — nuevo método privado

Añádelo **al final de la clase**, antes del `}` de cierre:

```java
private AccionNPC seleccionarAccionCBR(Player jugador) {
    // Exploración: con probabilidad FACTOR_EXPLORACION elige al azar
    if (memoria.isEmpty() || random.nextDouble() < FACTOR_EXPLORACION) {
        return accionAleatoria();
    }

    String contexto = "CERCA";

    // Suma recompensas por acción filtrando por contexto
    Map<AccionNPC, Integer> puntuaciones = new HashMap<>();
    for (CasoInteraccion caso : memoria) {
        if (caso.getContextoJugador().equals(contexto)) {
            puntuaciones.merge(caso.getAccionNPC(), caso.getRecompensa(), Integer::sum);
        }
    }

    if (puntuaciones.isEmpty()) {
        return accionAleatoria();
    }

    // Devuelve la acción con mayor puntuación (explotación)
    return puntuaciones.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .get()
            .getKey();
}
```

---

## 5. Método `ejecutarAccionNPC(Player jugador, AccionNPC accion)` — nuevo método privado

Añádelo **al final de la clase**, antes del `}` de cierre:

```java
private void ejecutarAccionNPC(Player jugador, AccionNPC accion) {
    switch (accion) {
        case SALUDAR ->
            jugador.sendMessage(NPC_Name + ": ¡Hola, " + jugador.getName() + "! Bienvenido.");
        case MOSTRAR_FLOR ->
            jugador.sendMessage(NPC_Name + ": *te ofrece una flor* 🌸");
        case MOSTRAR_ESPADA ->
            jugador.sendMessage(NPC_Name + ": *desenvaina la espada* ¡Mantén la distancia!");
        case HUIR ->
            jugador.sendMessage(NPC_Name + ": ¡Eek! *sale corriendo*");
    }
}
```

---

## 6. Método `evaluarReaccionJugador(Player jugador)` — nuevo método privado

Añádelo **al final de la clase**, antes del `}` de cierre:

```java
private int evaluarReaccionJugador(Player jugador) {
    if (jugador.isSneaking()) {
        return 1; // Reacción positiva: agachado = gesto amistoso
    }
    if (Tag.ITEMS_SWORDS.isTagged(jugador.getInventory().getItemInMainHand().getType())) {
        return -1; // Reacción negativa: lleva espada en mano
    }
    return 0;
}
```

---

## 7. Método `guardarCaso(Player jugador, AccionNPC accion)` — nuevo método privado

Añádelo **al final de la clase**, antes del `}` de cierre:

```java
private void guardarCaso(Player jugador, AccionNPC accion) {
    int recompensa = evaluarReaccionJugador(jugador);
    CasoInteraccion caso = new CasoInteraccion("CERCA", accion, recompensa);
    memoria.add(caso);
    getLogger().info("[CBR] Caso guardado: " + caso);
}
```

---

## 8. Modificar el bloque `if (jugadorCerca != null)` en `iniciarSistemaInteraccion()`

Busca este bloque dentro del método `iniciarSistemaInteraccion()`:

```java
Player jugadorCerca = encontrarJugadorCerca();
if (jugadorCerca != null) {
    detenerYSaludar(jugadorCerca);
    // Mirar al jugador
    npc.faceLocation(jugadorCerca.getLocation());
} else {
    reanudarPatrullaje();
}
```

**Reemplázalo** por esto:

```java
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
        AccionNPC accion = seleccionarAccionCBR(jugadorCerca);
        ejecutarAccionNPC(jugadorCerca, accion);

        // Guardar el caso 2 segundos después (40 ticks)
        Player jugadorRef = jugadorCerca;
        AccionNPC accionRef = accion;
        new BukkitRunnable() {
            @Override
            public void run() {
                guardarCaso(jugadorRef, accionRef);
            }
        }.runTaskLater(Soldado.this, 40L);
    }
} else {
    reanudarPatrullaje();
}
```

> **Nota:** El método `detenerYSaludar()` queda en desuso con este cambio. Puedes dejarlo en el archivo por ahora sin problema, ya no se llama.

---

## Resumen de archivos tocados

| Archivo                                          | Acción                                                   |
| ------------------------------------------------ | -------------------------------------------------------- |
| `src/main/java/edu/vic/Soldado.java`             | Modificar (imports, campos y métodos nuevos, bloque CBR) |
| `src/main/java/edu/vic/cbr/AccionNPC.java`       | No tocar (ya correcto)                                   |
| `src/main/java/edu/vic/cbr/CasoInteraccion.java` | No tocar (ya correcto)                                   |

Cuando termines, compila con `mvn clean compile` para verificar que no hay errores.

