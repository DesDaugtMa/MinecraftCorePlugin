package DesDaugtMa.core.listeners;

import DesDaugtMa.core.manager.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Interzeptiert den /stop Befehl, um benutzerdefinierte Logik auszuführen.
 */
public class StopOverrideListener implements Listener {

    private final MessageManager msg;

    public StopOverrideListener(MessageManager messageManager) {
        this.msg = messageManager;
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String cmd = event.getMessage().split(" ")[0].toLowerCase();
        if (cmd.equals("/stop") || cmd.equals("/minecraft:stop")) {
            if (!event.getPlayer().hasPermission("minecraft.command.stop")) return;
            event.setCancelled(true);
            performCustomStop(event.getPlayer());
        }
    }

    @EventHandler
    public void onConsoleCommand(ServerCommandEvent event) {
        String cmd = event.getCommand().split(" ")[0].toLowerCase();
        if (cmd.equals("stop") || cmd.equals("minecraft:stop")) {
            event.setCancelled(true);
            performCustomStop(event.getSender());
        }
    }

    private void performCustomStop(CommandSender sender) {
        msg.send(sender, "stop-blocked");

        // Logik zur Deaktivierung des Autorestarts via Datei
        File file = new File("autorestart.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("false");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bukkit.shutdown();
    }
}