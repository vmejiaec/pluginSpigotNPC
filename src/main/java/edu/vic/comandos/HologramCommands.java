package edu.vic.comandos;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

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
                "VER CRÉDITOS",
                "VER LOS CRÉDITOS"
        };
        int removedCount = 0;
        for (Entity entity : player.getWorld().getEntities()) {
            if (!(entity instanceof TextDisplay menuOption)) {
                continue;
            }
            String name = menuOption.getText();
            if (name == null) {
                continue;
            }
            for (String hologramaTexto : hologramasTextos) {
                if (name.contains(hologramaTexto)) {
                    menuOption.remove();
                    removedCount++;
                    break;
                }
            }
        }
        player.sendMessage("Eliminados " + removedCount + " hologramas.");
    }

}
