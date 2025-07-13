# Plugin de NPC Soldado para Spigot

## Descripción
Este plugin para Spigot permite crear un NPC soldado llamado "Aurora" que patrulla de forma autónoma entre dos puntos. La principal característica de este plugin es la **persistencia**: el NPC y su ruta de patrulla se guardan y se recargan automáticamente cada vez que el servidor se reinicia.

El NPC también interactúa con los jugadores cercanos. Si un jugador se acerca a menos de 5 bloques con una espada en la mano, el NPC le enviará un mensaje.

## Características Principales
*   **NPC Persistente:** El NPC no desaparece al reiniciar el servidor. Su identidad y sus puntos de patrulla se guardan en un archivo de configuración.
*   **Patrullaje Automático:** El NPC patrulla de forma continua entre un punto de inicio (donde se crea) y un punto final a 12 bloques de distancia.
*   **Detección de Jugadores:** El NPC detecta jugadores cercanos y reacciona si llevan una espada.
*   **Comandos Sencillos:** Incluye comandos fáciles de usar para crear y eliminar el NPC.
*   **Eficiencia:** Utiliza una única tarea asíncrona para detectar jugadores, garantizando un impacto mínimo en el rendimiento del servidor.

## Instalación
1.  **Requisito Previo:** Asegúrate de tener el plugin [Citizens](https://www.spigotmc.org/resources/citizens.1381/) instalado en tu servidor.
2.  Descarga la última versión del plugin (`Soldado.jar`) desde la sección de [releases](https://github.com/tu-usuario/tu-repositorio/releases).
3.  Copia el archivo `.jar` en la carpeta `plugins` de tu servidor Spigot.
4.  Reinicia el servidor. El plugin se activará automáticamente.

## ¿Cómo Funciona?

### Comandos
Los comandos son simples y directos:

*   `/crearnpc`
    *   **Función:** Crea el NPC "Aurora" en tu ubicación actual. Este punto se convierte en el inicio de su patrulla.
    *   **Ejemplo:**
        ```
        /crearnpc
        ```
        *Resultado:* El NPC aparecerá y comenzará a caminar hacia un punto a 12 bloques de distancia. Recibirás el mensaje: `¡NPC 'Aurora' creado y patrullando!`.

*   `/eliminarnpc`
    *   **Función:** Elimina permanentemente el NPC del servidor y de la configuración.
    *   **Ejemplo:**
        ```
        /eliminarnpc
        ```
        *Resultado:* El NPC desaparecerá y recibirás el mensaje: `NPC eliminado correctamente`.

### Interacción
Para que el NPC interactúe contigo, sigue estos pasos:
1.  Equipa cualquier tipo de espada en tu mano.
2.  Acércate a menos de 5 bloques del NPC.
3.  El NPC te enviará un mensaje, como: `Aurora: ¿Es eso una espada de diamante?`

### Persistencia
El plugin crea un archivo `config.yml` dentro de la carpeta del plugin (`plugins/Soldado/config.yml`). Este archivo almacena la ID del NPC y las coordenadas de sus puntos de patrulla.

**Ejemplo del `config.yml`:**
```yml
npc:
  id: 123
  puntoA:
    ==: org.bukkit.Location
    world: world
    x: 100.5
    y: 68.0
    z: 250.5
    pitch: 0.0
    yaw: 90.0
  puntoB:
    ==: org.bukkit.Location
    world: world
    x: 112.5
    y: 68.0
    z: 250.5
    pitch: 0.0
    yaw: 90.0
```
**No es necesario editar este archivo manualmente.** El plugin gestiona todo de forma automática.

## Compilación (Para Desarrolladores)
Si deseas compilar el plugin desde el código fuente, puedes usar Maven:
```bash
mvn clean package
```
El archivo `.jar` compilado se encontrará en la carpeta `target/`.