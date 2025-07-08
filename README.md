# NPC para patrullar entre dos puntos

## Descripción
Este plugin permite la creación de un NPC (Non-Player Character) que patrulla automáticamente entre dos puntos definidos. El primer punto es la ubicación donde el NPC es creado, y el segundo punto se establece a 12 bloques de distancia horizontal desde el punto de creación. Además, el NPC interactúa con los jugadores: si un jugador se aproxima al NPC llevando una espada en la mano, el NPC le enviará un mensaje.

## Características
*   **Creación de NPC Patrullero:** Genera un NPC que sigue una ruta de patrulla predefinida.
*   **Patrulla de Dos Puntos:** El NPC se mueve entre su punto de creación y un segundo punto a 12 bloques de distancia horizontal.
*   **Detección de Jugadores:** Identifica a los jugadores que se acercan al NPC.
*   **Interacción con Espada:** Si el jugador detectado lleva una espada, el NPC le envía un mensaje personalizado.

## Instalación
Para instalar este plugin en tu servidor Spigot, sigue estos pasos:

1.  Asegúrate de tener instalado el plugin **Citizens** en tu servidor, ya que es una dependencia necesaria para el funcionamiento de este plugin.
2.  Descarga el archivo `.jar` de este plugin.
3.  Copia el archivo `.jar` descargado en la carpeta `plugins` de tu servidor Spigot.
4.  Reinicia o recarga tu servidor para que el plugin se active.

## Uso
Una vez instalado y activado, puedes usar el plugin con los siguientes comandos:

*   `/crearnpc`: Crea un nuevo NPC en tu ubicación actual. Este punto será el inicio de su ruta de patrulla. El segundo punto de patrulla se establecerá automáticamente a 12 bloques de distancia horizontal.
*   `/eliminarnpcs`: Elimina todos los NPCs creados por este plugin.

**Interacción con el NPC:**
Para recibir un mensaje del NPC, simplemente acércate a él mientras llevas una espada en tu mano principal.

## Compilación (para desarrolladores)
Este proyecto es un proyecto Maven. Para compilarlo desde el código fuente, puedes usar uno de los siguientes métodos:

*   **Usando Maven:** Abre una terminal en la raíz del proyecto y ejecuta el siguiente comando:
    ```bash
    mvn clean package
    ```
    Esto generará el archivo `.jar` del plugin en el directorio `target/`.

*   **Usando el script de compilación:** Puedes ejecutar el archivo `CrearPublicarPlugin.bat` ubicado en la raíz del proyecto. Este script automatiza el proceso de compilación.
