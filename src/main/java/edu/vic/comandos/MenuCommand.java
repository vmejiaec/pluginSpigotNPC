package edu.vic.comandos;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import edu.vic.menus.MenuManager;

public class MenuCommand implements CommandExecutor {

    public MenuCommand() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player))
            return false;

        if (label.equalsIgnoreCase("menustart")) {
            MenuManager.openStartMenu(player);
            player.sendMessage("Menu abierto");
            return true;
        }

        return false;
    }
}
