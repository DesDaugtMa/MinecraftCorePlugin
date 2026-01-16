package DesDaugtMa.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class StopOverrideListener implements Listener {

    // Wird gefeuert, wenn ein SPIELER einen Befehl eingibt
    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage();
        String command = message.split(" ")[0];

        if (command.equalsIgnoreCase("/stop") || command.equalsIgnoreCase("/minecraft:stop")) {
            // Berechtigung prüfen (Standard Bukkit Permission)
            if (!event.getPlayer().hasPermission("minecraft.command.stop")) {
                // Wenn er keine Rechte hat, lassen wir Bukkit das regeln (sendet "Unknown command" oder "No permission")
                return;
            }

            // Befehl abbrechen, damit wir unsere eigene Logik ausführen können
            event.setCancelled(true);
            performCustomStop(event.getPlayer());
        }
    }

    // Wird gefeuert, wenn die KONSOLE einen Befehl eingibt
    @EventHandler
    public void onConsoleCommand(ServerCommandEvent event) {
        String command = event.getCommand().split(" ")[0];

        if (command.equalsIgnoreCase("stop") || command.equalsIgnoreCase("minecraft:stop")) {
            event.setCancelled(true);
            performCustomStop(event.getSender());
        }
    }

    /**
     * Schreibt "false" in die autorestart.txt und stoppt den Server.
     */
    private void performCustomStop(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + "Der Server wird gestoppt. Autostart wird deaktiviert...");

        // 1. In die Datei schreiben
        File file = new File("autorestart.txt"); // Liegt im Server-Root neben der spigot.jar

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("false");
            sender.sendMessage(ChatColor.GREEN + "Datei 'autorestart.txt' wurde auf 'false' gesetzt.");
        } catch (IOException e) {
            sender.sendMessage(ChatColor.RED + "Fehler beim Schreiben der 'autorestart.txt'!");
            e.printStackTrace();
            // Wir stoppen trotzdem, auch wenn das Schreiben fehlschlug
        }

        // 2. Server herunterfahren
        Bukkit.shutdown();
    }
}
