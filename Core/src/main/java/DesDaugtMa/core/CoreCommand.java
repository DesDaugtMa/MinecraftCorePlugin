package DesDaugtMa.core;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CoreCommand implements CommandExecutor {

    private final Core plugin;
    private final MessageManager msg;
    private final AutoRestartScheduler autoRestartScheduler;
    private final MotdManager motdManager; // NEU

    // Konstruktor erweitert
    public CoreCommand(Core plugin, MessageManager msg, AutoRestartScheduler scheduler, MotdManager motdManager) {
        this.plugin = plugin;
        this.msg = msg;
        this.autoRestartScheduler = scheduler;
        this.motdManager = motdManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("simpletablist.admin")) {
            msg.send(sender, "no-permission");
            return true;
        }

        if (args.length == 0) {
            msg.send(sender, "unknown-subcommand");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            // 1. Bukkit Config reload
            plugin.reloadConfig();

            // 2. Manager aktualisieren
            autoRestartScheduler.loadSettings();
            motdManager.reload(); // NEU: MOTD neu berechnen (Zentrierung etc.)

            msg.send(sender, "reload-success");
            return true;
        }

        msg.send(sender, "unknown-subcommand");
        return true;
    }
}
