package edu.vic.menus;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class MenuManager {
    private static StartMenu startMenu;
    private static GameplayMenu gameplayMenu;
    private static GameOverMenu gameOverMenu;
    private static CreditsMenu creditsMenu;

    public static void init(Plugin plugin) {
        startMenu = new StartMenu(plugin);
        gameplayMenu = new GameplayMenu(plugin);
        gameOverMenu = new GameOverMenu(plugin);
        creditsMenu = new CreditsMenu(plugin);
    }

    public static void openStartMenu(Player player) {
        startMenu.open(player);
    }

    public static void openGameOverMenu(Player player) {
        gameOverMenu.open(player);
    }

    public static void openGameplayMenu(Player player) {
        gameplayMenu.open(player);
    }

    public static void openCreditsMenu(Player player) {
        creditsMenu.open(player);
    }
}
