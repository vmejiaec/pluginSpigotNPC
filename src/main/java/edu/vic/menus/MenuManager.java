package edu.vic.menus;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;

public class MenuManager {
    private final Map<String, MenuPair> menuOptions;

    public MenuManager() {
        menuOptions = new HashMap<>();
    }

    // Devuelve la opción de menú dado el id
    public MenuPair getMenuOption(String id) {
        return menuOptions.get(id);
    }

    // Devuelve el id dada la opción de menú
    public String getMenuOptionId(MenuPair menuOption) {
        for (Map.Entry<String, MenuPair> entry : menuOptions.entrySet()) {
            if (entry.getValue().equals(menuOption)) {
                return entry.getKey();
            }
        }
        return null;
    }

    // Verifica si es una opción de menú
    public boolean isMenuOption(MenuPair menuOption) {
        return menuOptions.containsValue(menuOption);
    }

    // Borra una opción de menú
    public boolean removeMenuOption(String id) {
        MenuPair menuOption = getMenuOption(id);
        if (menuOption != null) {
            menuOption.remove();
            menuOptions.remove(id);
            return true;
        }
        return false;
    }

    // Borra todas las opciones de menú
    public void removeAllMenuOptions() {
        for (MenuPair menuOption : menuOptions.values()) {
            menuOption.remove();
        }
        menuOptions.clear();
    }

    // Actualiza una opción de menú
    public boolean updateMenuOption(String id, MenuPair newMenuOption) {
        MenuPair menuOption = getMenuOption(id);
        if (menuOption != null) {
            menuOption = newMenuOption;
            return true;
        }
        return false;
    }

    // Retorna un conjunto con los ids de las opciones de menú
    public Set<String> getAllMenuOptionsIds() {
        return menuOptions.keySet();
    }

    // Crea una opción de menú
    public MenuPair createMenuOption(Location location, String texto, String id) {
        if (menuOptions.containsKey(id)) {
            removeMenuOption(id);
        }
        MenuPair menuOption = new MenuPair(location, texto);
        menuOptions.put(id, menuOption);
        return menuOption;
    }

}
