package edu.vic.menus;

import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

public class MenuText {
    public TextDisplay text;

    public MenuText(Location loc, String texto) {
        text = (TextDisplay) loc.getWorld()
                .spawnEntity(loc, EntityType.TEXT_DISPLAY);
        text.setText("§b► §f" + texto);
        text.setSeeThrough(false);
        text.setBillboard(Display.Billboard.FIXED);
        Transformation transformation = new Transformation(
                new Vector3f(0, 0, 0),
                new AxisAngle4f((float) Math.PI, 0, 1, 0), // Rotación 180 grados en Y
                new Vector3f(1.6f, 1.6f, 1.6f), // Más grande
                new AxisAngle4f());
        text.setTransformation(transformation);
    }

    public void remove() {
        text.remove();
    }
}
