package DesDaugtMa.core;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RestartCommand implements CommandExecutor {

    private final RestartManager restartManager;
    private final MessageManager msg;

    public RestartCommand(RestartManager restartManager, MessageManager messageManager) {
        this.restartManager = restartManager;
        this.msg = messageManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("simpletablist.restart")) {
            msg.send(sender, "no-permission");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("§cBenutzung: /neustart <Sekunden> ODER /neustart abbrechen"); // Kann man auch in Config machen, wenn man will
            return true;
        }

        if (args[0].equalsIgnoreCase("abbrechen")) {
            if (restartManager.isRunning()) {
                restartManager.cancelRestart();

                msg.broadcast("restart-broadcast-aborted");

                // Actionbar leeren (Manuell, da MessageManager nur Text sendet)
                TextComponent empty = new TextComponent("");
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, empty);
                }

                msg.send(sender, "restart-aborted-sender");
            } else {
                msg.send(sender, "restart-no-timer");
            }
            return true;
        }

        try {
            int seconds = Integer.parseInt(args[0]);
            if (seconds <= 0) {
                // Man könnte hier auch ne Config Nachricht machen
                sender.sendMessage("§cZeit muss > 0 sein.");
                return true;
            }

            if (restartManager.isRunning()) {
                msg.send(sender, "restart-updated", "%seconds%", String.valueOf(seconds));
            } else {
                msg.send(sender, "restart-started", "%seconds%", String.valueOf(seconds));
            }

            restartManager.startRestart(seconds);

        } catch (NumberFormatException e) {
            msg.send(sender, "invalid-number");
        }

        return true;
    }
}