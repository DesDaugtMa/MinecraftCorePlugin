package DesDaugtMa.core;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RestartCommand implements CommandExecutor {

    private final RestartManager restartManager;

    public RestartCommand(RestartManager restartManager) {
        this.restartManager = restartManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("simpletablist.restart")) {
            sender.sendMessage(ChatColor.RED + "Dazu hast du keine Rechte!");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Benutzung: /neustart <Sekunden> ODER /neustart abbrechen");
            return true;
        }

        // --- Abbrechen ---
        if (args[0].equalsIgnoreCase("abbrechen")) {
            if (restartManager.isRunning()) {
                restartManager.cancelRestart();

                Bukkit.broadcastMessage(ChatColor.GREEN + "Der geplante Neustart wurde abgebrochen!");

                // Actionbar leeren
                TextComponent empty = new TextComponent("");
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, empty);
                }

                sender.sendMessage(ChatColor.GREEN + "Timer gestoppt.");
            } else {
                sender.sendMessage(ChatColor.RED + "Es läuft aktuell kein Countdown.");
            }
            return true;
        }

        // --- Starten / Update ---
        try {
            int seconds = Integer.parseInt(args[0]);
            if (seconds <= 0) {
                sender.sendMessage(ChatColor.RED + "Zeit muss > 0 sein.");
                return true;
            }

            if (restartManager.isRunning()) {
                sender.sendMessage(ChatColor.YELLOW + "Countdown aktualisiert auf " + ChatColor.GOLD + seconds + "s" + ChatColor.YELLOW + "!");
            } else {
                sender.sendMessage(ChatColor.GREEN + "Countdown (" + seconds + "s) gestartet!");
            }

            // Hier delegieren wir an den Manager!
            restartManager.startRestart(seconds);

        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Ungültige Zahl.");
        }

        return true;
    }
}