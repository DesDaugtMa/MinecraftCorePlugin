package DesDaugtMa.core;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    private final SpawnManager spawnManager;

    public SpawnCommand(SpawnManager spawnManager) {
        this.spawnManager = spawnManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Dieser Befehl ist nur für Spieler!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("simpletablist.setspawn")) {
            player.sendMessage(ChatColor.RED + "Dazu hast du keine Rechte!");
            return true;
        }

        // Location holen und speichern
        spawnManager.setSpawn(player.getLocation());

        player.sendMessage(ChatColor.GREEN + "Spawnpunkt erfolgreich gesetzt!");

        return true;
    }
}
