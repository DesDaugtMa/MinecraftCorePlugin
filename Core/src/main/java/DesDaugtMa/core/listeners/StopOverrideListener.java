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
 * Interzeptiert /stop und /restart Befehle, um die autorestart.txt zu steuern.
 */
public class StopOverrideListener implements Listener {

    private final MessageManager msg;

    public StopOverrideListener(MessageManager messageManager) {
        this.msg = messageManager;
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage();
        String cmd = message.split(" ")[0].toLowerCase();

        // STOP Logik
        if (cmd.equals("/stop") || cmd.equals("/minecraft:stop")) {
            if (!event.getPlayer().hasPermission("minecraft.command.stop")) return;
            event.setCancelled(true);
            performShutdown(event.getPlayer(), false); // false = Kein Restart
        }

        // RESTART Logik
        else if (cmd.equals("/restart") || cmd.equals("/bukkit:restart") || cmd.equals("/spigot:restart")) {
            if (!event.getPlayer().hasPermission("bukkit.command.restart")) return;
            event.setCancelled(true);
            performShutdown(event.getPlayer(), true); // true = Restart erwünscht
        }
    }

    @EventHandler
    public void onConsoleCommand(ServerCommandEvent event) {
        String cmd = event.getCommand().split(" ")[0].toLowerCase();

        // STOP Logik
        if (cmd.equals("stop") || cmd.equals("minecraft:stop")) {
            event.setCancelled(true);
            performShutdown(event.getSender(), false);
        }

        // RESTART Logik
        else if (cmd.equals("restart") || cmd.equals("bukkit:restart") || cmd.equals("spigot:restart")) {
            event.setCancelled(true);
            performShutdown(event.getSender(), true);
        }
    }

    /**
     * Führt den Shutdown durch und schreibt die autorestart.txt
     * @param sender Der Auslöser des Befehls
     * @param autoRestart true für "true" in der Datei, false für "false"
     */
    private void performShutdown(CommandSender sender, boolean autoRestart) {
        // Nachricht senden
        if (autoRestart) {
            msg.send(sender, "restart-triggered");
        } else {
            msg.send(sender, "stop-blocked");
        }

        // Datei schreiben (true oder false)
        setAutorestartFile(autoRestart);

        // Server herunterfahren (das Wrapper-Skript prüft dann die Datei)
        Bukkit.shutdown();
    }

    private void setAutorestartFile(boolean active) {
        File file = new File("autorestart.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(active ? "true" : "false");
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("Konnte autorestart.txt nicht schreiben!");
        }
    }
}