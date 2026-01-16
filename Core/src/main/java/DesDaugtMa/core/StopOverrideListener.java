package DesDaugtMa.core;

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

public class StopOverrideListener implements Listener {

    private final MessageManager msg;

    public StopOverrideListener(MessageManager messageManager) {
        this.msg = messageManager;
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().split(" ")[0];
        if (command.equalsIgnoreCase("/stop") || command.equalsIgnoreCase("/minecraft:stop")) {
            if (!event.getPlayer().hasPermission("minecraft.command.stop")) return;

            event.setCancelled(true);

            // Nachricht senden
            msg.send(event.getPlayer(), "stop-blocked");

            performCustomStop(event.getPlayer());
        }
    }

    @EventHandler
    public void onConsoleCommand(ServerCommandEvent event) {
        String command = event.getCommand().split(" ")[0];
        if (command.equalsIgnoreCase("stop") || command.equalsIgnoreCase("minecraft:stop")) {
            event.setCancelled(true);
            msg.send(event.getSender(), "stop-console-blocked");
            performCustomStop(event.getSender());
        }
    }

    private void performCustomStop(CommandSender sender) {
        msg.send(sender, "stop-custom-executed");

        File file = new File("autorestart.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("false");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bukkit.shutdown();
    }
}
