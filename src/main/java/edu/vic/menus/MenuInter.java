package edu.vic.menus;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;

public class MenuInter {
    public Interaction inter;

    public MenuInter(Location loc) {
        inter = (Interaction) loc.getWorld()
                .spawnEntity(loc, EntityType.INTERACTION);
        inter.setInteractionHeight(1.0f);
        inter.setInteractionWidth(1.0f);
    }

    public void remove() {
        inter.remove();
    }
}
