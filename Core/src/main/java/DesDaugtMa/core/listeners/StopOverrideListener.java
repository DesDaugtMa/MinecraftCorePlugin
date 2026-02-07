package DesDaugtMa.core.listeners;

import DesDaugtMa.core.manager.GoogleDriveUploader;
import DesDaugtMa.core.manager.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class StopOverrideListener implements Listener {

    private final JavaPlugin plugin; // Hinzugefügt
    private final MessageManager msg;

    // Konstruktor angepasst: Braucht jetzt plugin Instanz
    public StopOverrideListener(JavaPlugin plugin, MessageManager messageManager) {
        this.plugin = plugin;
        this.msg = messageManager;
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage();
        String cmd = message.split(" ")[0].toLowerCase();

        if (cmd.equals("/stop") || cmd.equals("/minecraft:stop")) {
            if (!event.getPlayer().hasPermission("minecraft.command.stop")) return;
            event.setCancelled(true);
            performShutdown(event.getPlayer(), false);
        } else if (cmd.equals("/restart") || cmd.equals("/bukkit:restart") || cmd.equals("/spigot:restart")) {
            if (!event.getPlayer().hasPermission("bukkit.command.restart")) return;
            event.setCancelled(true);
            performShutdown(event.getPlayer(), true);
        }
    }

    @EventHandler
    public void onConsoleCommand(ServerCommandEvent event) {
        String cmd = event.getCommand().split(" ")[0].toLowerCase();

        if (cmd.equals("stop") || cmd.equals("minecraft:stop")) {
            event.setCancelled(true);
            performShutdown(event.getSender(), false);
        } else if (cmd.equals("restart") || cmd.equals("bukkit:restart") || cmd.equals("spigot:restart")) {
            event.setCancelled(true);
            performShutdown(event.getSender(), true);
        }
    }

    private void performShutdown(CommandSender sender, boolean autoRestart) {
        // Prüfen ob Upload gewünscht ist (nur bei Restart und wenn in Config an)
        boolean doUpload = autoRestart && plugin.getConfig().getBoolean("backup.upload-on-restart", false);

        if (doUpload) {
            msg.send(sender, "backup-upload-start");
            // Optional: Alle Spieler kicken, damit niemand mehr was macht
            Bukkit.getOnlinePlayers().forEach(p -> p.kickPlayer("§cServer startet neu... (Backup Upload)"));

            // Asynchron starten, damit der Main-Thread nicht blockiert (Timeout Watchdog)
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                try {
                    File backupDir = new File(plugin.getConfig().getString("backup.folder-path", "./Backups"));
                    File latestZip = getLatestZip(backupDir);

                    if (latestZip != null) {
                        GoogleDriveUploader uploader = new GoogleDriveUploader(plugin);
                        uploader.uploadFile(latestZip);
                        // Nachricht muss synchron gesendet werden (optional, da Server eh gleich ausgeht)
                        // msg.broadcast("backup-upload-success");
                    } else {
                        plugin.getLogger().warning("Kein Zip-Archiv im Backup Ordner gefunden!");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    msg.send(sender, "backup-upload-fail");
                } finally {
                    // WICHTIG: Shutdown wieder auf den Main-Thread schieben!
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        finalizeShutdown(sender, autoRestart);
                    });
                }
            });
        } else {
            // Normaler Shutdown ohne Upload
            finalizeShutdown(sender, autoRestart);
        }
    }

    // Die eigentliche Shutdown Logik (ausgelagert)
    private void finalizeShutdown(CommandSender sender, boolean autoRestart) {
        if (autoRestart) {
            msg.send(sender, "restart-triggered");
        } else {
            msg.send(sender, "stop-blocked");
        }
        setAutorestartFile(autoRestart);
        Bukkit.shutdown();
    }

    private File getLatestZip(File dir) {
        if (!dir.exists() || !dir.isDirectory()) return null;

        File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".zip"));
        if (files == null || files.length == 0) return null;

        // Sortieren nach letzter Änderung (neueste zuerst)
        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());

        return files[0];
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