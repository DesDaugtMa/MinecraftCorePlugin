package DesDaugtMa.core.commands;

import DesDaugtMa.core.manager.MessageManager;
import DesDaugtMa.core.manager.SpawnManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    private final SpawnManager spawnManager;
    private final MessageManager msg;

    public SpawnCommand(SpawnManager spawnManager, MessageManager messageManager) {
        this.spawnManager = spawnManager;
        this.msg = messageManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            msg.send(sender, "not-a-player");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("simpletablist.setspawn")) {
            msg.send(player, "no-permission");
            return true;
        }

        spawnManager.setSpawn(player.getLocation());
        msg.send(player, "spawn-set");

        return true;
    }
}
