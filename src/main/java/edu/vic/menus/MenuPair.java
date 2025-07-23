package edu.vic.menus;

import org.bukkit.Location;

public class MenuPair {
    private final MenuText menuText;
    private final MenuInter menuInter;
    private Location loc;

    public MenuPair(MenuText menuText, MenuInter menuInter) {
        this.menuText = menuText;
        this.menuInter = menuInter;
    }

    public MenuPair(Location loc, String texto) {
        this.loc = loc;
        this.menuText = new MenuText(loc, texto);
        this.menuInter = new MenuInter(loc);
    }

    public MenuText getMenuText() {
        return menuText;
    }

    public MenuInter getMenuInter() {
        return menuInter;
    }

    public Location getLocation() {
        return loc;
    }

    public void remove() {
        if (menuInter != null)
            menuInter.remove();
        if (menuText != null)
            menuText.remove();
    }
}
