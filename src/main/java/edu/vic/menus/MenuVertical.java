package edu.vic.menus;

import org.bukkit.Location;

public class MenuVertical {

    private MenuManager manager;

    public MenuVertical(MenuManager manager) {
        this.manager = manager;
    }

    public boolean crearMenuVertical(Location base) {
        base = base.clone().add(0, 2, 2);

        Location pos1 = base.clone();
        Location pos2 = base.clone().add(0, -0.8, 0);
        Location pos3 = base.clone().add(0, -1.6, 0);
        Location pos4 = base.clone().add(0, -2.4, 0);

        manager.createMenuOption(pos1, "INICIAR JUEGO", "iniciar_juego");
        manager.createMenuOption(pos2, "VER PUNTUACIÓN", "ver_puntuacion");
        manager.createMenuOption(pos3, "TERMINAR JUEGO", "terminar_juego");
        manager.createMenuOption(pos4, "VER CRÉDITOS", "creditos");

        return true;
    }
}
