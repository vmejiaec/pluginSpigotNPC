# SpeedCraft - Definición del Juego

## 🎮 Descripción General

**SpeedCraft** es un minijuego competitivo para Minecraft que desafía a los jugadores a completar tareas de construcción y crafteo bajo presión temporal. El juego combina velocidad, precisión y conocimiento del sistema de crafteo de Minecraft para crear una experiencia emocionante y educativa.

### Concepto Principal
Los jugadores deben completar una serie de recetas de crafteo en el menor tiempo posible, utilizando materiales limitados y enfrentándose a desafíos progresivamente más complejos.

---

## 🏗️ Mecánicas del Juego

### Sistema de Rondas
- **Ronda de Calentamiento**: 3 recetas básicas (madera → tablas, palos, etc.)
- **Rondas Progresivas**: Aumenta la complejidad (herramientas → armaduras → redstone)
- **Ronda Final**: Recetas complejas combinadas con tiempo límite extremo

### Mecánicas de Juego
1. **Inventario Limitado**: Los jugadores reciben materiales específicos para cada ronda
2. **Tiempo Límite**: Cada ronda tiene un tiempo máximo decreciente
3. **Penalizaciones**: Errores en crafteo reducen puntos o añaden tiempo
4. **Power-ups**: Bonificaciones especiales por velocidad o precisión
5. **Multiplicadores**: Bonus por completar rondas consecutivas sin errores

### Sistema de Puntuación
- **Puntos Base**: Por completar cada receta correctamente
- **Bonus de Velocidad**: Más puntos por menos tiempo usado
- **Combo Multiplier**: x2, x3, x4 por recetas consecutivas
- **Penalty**: -50% puntos por errores o tiempo agotado

---

## 🔧 Arquitectura Técnica del Plugin

### 📁 Estructura de Paquetes Propuesta

```
edu.vic.speedcraft/
├── core/                          # Núcleo del sistema de juego
│   ├── GameManager.java           # Controlador principal del juego
│   ├── GameState.java             # Estados del juego (WAITING, PLAYING, ENDED)
│   ├── GameSession.java           # Sesión individual de juego
│   └── GameConfig.java            # Configuración general del juego
├── player/                        # Sistema de jugadores
│   ├── SpeedCraftPlayer.java      # Wrapper del jugador con stats
│   ├── PlayerManager.java         # Gestión de jugadores en partida
│   └── PlayerStats.java           # Estadísticas y progreso
├── rounds/                        # Sistema de rondas
│   ├── Round.java                 # Clase abstracta de ronda
│   ├── WarmupRound.java           # Ronda de calentamiento
│   ├── StandardRound.java         # Rondas normales
│   ├── FinalRound.java            # Ronda final
│   └── RoundManager.java          # Controlador de rondas
├── recipes/                       # Sistema de recetas
│   ├── CraftingChallenge.java     # Desafío de crafteo individual
│   ├── RecipeValidator.java       # Validador de recetas completadas
│   ├── RecipeProvider.java        # Proveedor de recetas por dificultad
│   └── RecipeReward.java          # Sistema de recompensas
├── scoring/                       # Sistema de puntuación
│   ├── ScoreCalculator.java       # Calculador de puntos
│   ├── Leaderboard.java           # Tabla de clasificación
│   └── ScoreboardManager.java     # Gestor del scoreboard visual
├── ui/                           # Interfaz de usuario
│   ├── GameGUI.java              # Interfaces gráficas del juego
│   ├── HologramDisplay.java      # Hologramas informativos
│   ├── ScoreboardDisplay.java    # Display del scoreboard
│   └── TimerDisplay.java         # Visualización de tiempo
├── events/                       # Sistema de eventos
│   ├── GameEventListener.java    # Listener principal del juego
│   ├── CraftingEventHandler.java # Manejo de eventos de crafteo
│   └── PlayerEventHandler.java   # Eventos específicos del jugador
├── storage/                      # Persistencia de datos
│   ├── GameDataManager.java      # Gestión de datos del juego
│   ├── StatsStorage.java         # Almacenamiento de estadísticas
│   └── ConfigLoader.java         # Carga de configuración
└── commands/                     # Comandos del juego
    ├── SpeedCraftCommand.java    # Comando principal /speedcraft
    ├── AdminCommands.java        # Comandos de administración
    └── PlayerCommands.java       # Comandos para jugadores
```

---

## 🎯 Arquitectura General de Juegos para Minecraft Server

### 1. **Patrón de Estados (State Pattern)**
```java
public enum GameState {
    WAITING,        // Esperando jugadores
    STARTING,       // Cuenta regresiva inicial
    PLAYING,        // Juego en progreso
    PAUSED,         // Juego pausado
    ENDING,         // Finalizando juego
    ENDED           // Juego terminado
}
```

### 2. **Sistema de Gestión de Jugadores**
- **PlayerManager**: Registro y seguimiento de jugadores
- **GamePlayer**: Wrapper que extiende funcionalidad del Player de Bukkit
- **Team System**: Gestión de equipos (si aplica)
- **Permission System**: Control de permisos durante el juego

### 3. **Sistema de Eventos Personalizados**
```java
// Eventos específicos del juego
public class GameStartEvent extends Event { ... }
public class PlayerJoinGameEvent extends Event { ... }
public class RoundCompleteEvent extends Event { ... }
public class GameEndEvent extends Event { ... }
```

### 4. **Gestión de Mundos y Áreas**
- **Arena System**: Definición de áreas de juego
- **World Management**: Creación/restauración de mundos temporales
- **Region Protection**: Protección de áreas durante el juego
- **Teleportation System**: Movimiento de jugadores entre áreas

### 5. **Sistema de Configuración**
```yaml
# config.yml ejemplo
speedcraft:
  game:
    min_players: 2
    max_players: 8
    warmup_time: 30
    round_time: 120
  scoring:
    base_points: 100
    speed_bonus_multiplier: 1.5
    error_penalty: 0.5
  arenas:
    - name: "arena1"
      world: "speedcraft_world"
      spawn: [0, 64, 0]
```

### 6. **Sistema de Persistencia**
- **Database Integration**: MySQL/SQLite para stats persistentes
- **File Storage**: YAML/JSON para configuraciones
- **Cache System**: Redis para datos temporales (opcional)
- **Backup System**: Respaldo automático de datos

### 7. **Sistema de Recompensas**
- **Economy Integration**: Vault API para economía
- **Item Rewards**: Items personalizados como premios
- **Title/Achievement System**: Títulos y logros
- **Leaderboard System**: Rankings globales y temporales

### 8. **Sistema de Comunicación**
- **ActionBar Messages**: Mensajes en la barra de acción
- **Title/Subtitle API**: Títulos grandes en pantalla
- **Chat Integration**: Canales de chat específicos del juego
- **Sound System**: Efectos sonoros inmersivos

---

## 🚀 Flujo de Ejecución del Juego

### 1. **Inicialización**
```
GameManager.init() → Cargar configuración → Preparar arenas → Registrar listeners
```

### 2. **Lobby/Espera**
```
Jugadores se unen → Verificar mínimo → Mostrar contador → Preparar inventarios
```

### 3. **Inicio del Juego**
```
Cuenta regresiva → Teletransportar jugadores → Limpiar inventarios → Iniciar timer
```

### 4. **Ejecución de Rondas**
```
Para cada ronda:
  → Mostrar receta objetivo
  → Dar materiales
  → Iniciar timer de ronda
  → Monitorear progreso
  → Calcular puntuación
  → Mostrar resultados
```

### 5. **Finalización**
```
Mostrar ganador → Actualizar stats → Dar recompensas → Limpiar arena → Reset
```

---

## 🔧 Componentes Técnicos Específicos

### **GameManager (Singleton)**
- Control centralizado del juego
- Gestión de estados
- Coordinación entre componentes
- Programación de tareas asíncronas

### **Round System**
```java
public abstract class Round {
    protected int timeLimit;
    protected List<CraftingChallenge> challenges;
    protected Map<Player, RoundResult> results;
    
    public abstract void start();
    public abstract void end();
    public abstract boolean isComplete();
}
```

### **Recipe Validation**
```java
public class RecipeValidator {
    public boolean validateCrafting(Player player, ItemStack result, ItemStack[] matrix);
    public CraftingResult calculateScore(long timeUsed, boolean correct);
    public void updatePlayerProgress(Player player, CraftingResult result);
}
```

### **Timer System**
```java
public class GameTimer {
    private BukkitRunnable currentTimer;
    
    public void startRoundTimer(int seconds, Runnable onComplete);
    public void startCountdown(int seconds, Consumer<Integer> onTick);
    public void pauseTimer();
    public void resumeTimer();
}
```

---

## 📊 Sistemas de Monitoreo y Debug

### **Logging System**
- Logs detallados de eventos del juego
- Métricas de rendimiento
- Error tracking y reporting
- Player behavior analytics

### **Admin Tools**
- Comandos de debug (`/speedcraft debug`)
- Visualización de estados internos
- Herramientas de testing
- Simulación de escenarios

### **Performance Monitoring**
- TPS impact monitoring
- Memory usage tracking
- Player count optimization
- Resource cleanup validation

---

## 🎨 Experiencia del Usuario

### **Visual Feedback**
- Hologramas con información en tiempo real
- Partículas para feedback visual
- Scoreboards dinámicos
- Progress bars visuales

### **Audio Feedback**
- Sonidos de confirmación para crafteos correctos
- Alertas sonoras para tiempo limitado
- Música de fondo atmosférica
- Efectos de éxito/fracaso

### **Responsive Design**
- Adaptación a diferentes números de jugadores
- Escalabilidad de dificultad
- Personalización de configuración
- Múltiples modos de juego

---

Este documento sirve como base arquitectónica para implementar SpeedCraft y puede ser extendido con más detalles específicos según las necesidades del desarrollo.
