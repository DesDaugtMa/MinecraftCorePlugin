package DesDaugtMa.core.commands;

import DesDaugtMa.core.Core;
import DesDaugtMa.core.manager.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CoreCommand implements CommandExecutor {

    private final Core plugin;
    private final MessageManager msg;

    // Konstruktor: AutoRestartScheduler wurde entfernt
    public CoreCommand(Core plugin, MessageManager msg) {
        this.plugin = plugin;
        this.msg = msg;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("core.admin")) {
            msg.send(sender, "no-permission");
            return true;
        }

        if (args.length == 0 || !args[0].equalsIgnoreCase("reload")) {
            msg.send(sender, "unknown-subcommand");
            return true;
        }

        // 1. Bukkit Config reload
        plugin.reloadConfig();

        // Hinweis: Da AutoRestartScheduler gelöscht wurde,
        // muss hier kein scheduler.loadSettings() mehr aufgerufen werden.

        msg.send(sender, "reload-success");
        return true;
    }
}