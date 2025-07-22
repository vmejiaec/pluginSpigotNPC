package edu.vic.comandos;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HologramCommands implements CommandExecutor {

    public HologramCommands() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        // Comando para borrar todos los hologramas huérfanos
        if (label.equalsIgnoreCase("eliminartodoholograma")) {
            eliminarTodoHolograma(player);
            return true;
        }

        return false;
    }

    public void eliminarTodoHolograma(Player player) {
        String[] hologramasTextos = {
                "INICIAR JUEGO",
                "VER PUNTUACIÓN",
                "TERMINAR JUEGO",
                "VER LOS CRÉDITOS"
        };
        int removedCount = 0;
        for (Entity entity : player.getWorld().getEntities()) {
            if (!(entity instanceof ArmorStand armorStand)) {
                continue;
            }
            String name = armorStand.getCustomName();
            if (name == null) {
                continue;
            }
            for (String hologramaTexto : hologramasTextos) {
                if (name.contains(hologramaTexto)) {
                    armorStand.remove();
                    removedCount++;
                    break;
                }
            }
        }
        player.sendMessage("Eliminados " + removedCount + " hologramas.");
    }

}
