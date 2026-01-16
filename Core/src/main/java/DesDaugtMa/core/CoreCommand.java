package DesDaugtMa.core;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CoreCommand implements CommandExecutor {

    private final Core plugin;
    private final MessageManager msg;
    private final AutoRestartScheduler autoRestartScheduler;

    public CoreCommand(Core plugin, MessageManager msg, AutoRestartScheduler scheduler) {
        this.plugin = plugin;
        this.msg = msg;
        this.autoRestartScheduler = scheduler;
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

        // Subcommand: reload
        if (args[0].equalsIgnoreCase("reload")) {
            // 1. Config von der Festplatte neu laden
            plugin.reloadConfig();

            // 2. Klassen aktualisieren, die Werte cachen
            autoRestartScheduler.loadSettings();

            // (MessageManager, SpawnManager und TabListUpdater lesen live aus der Config,
            // daher müssen wir die nicht explizit reloaden, reloadConfig() reicht dort.)

            msg.send(sender, "reload-success");
            return true;
        }

        msg.send(sender, "unknown-subcommand");
        return true;
    }
}
