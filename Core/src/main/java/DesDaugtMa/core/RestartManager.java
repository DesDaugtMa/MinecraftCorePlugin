package DesDaugtMa.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class RestartManager {

    private final JavaPlugin plugin;
    private RestartTimer currentTimer;

    public RestartManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // Startet oder Aktualisiert den Neustart
    public void startRestart(int seconds) {
        // Falls schon einer läuft: Stoppen (für Update)
        if (isRunning()) {
            currentTimer.cancel();
        }

        // Neuen Timer erstellen
        currentTimer = new RestartTimer(seconds);
        currentTimer.runTaskTimer(plugin, 0L, 20L);
    }

    // Bricht den Neustart ab
    public void cancelRestart() {
        if (isRunning()) {
            currentTimer.cancel();
            currentTimer = null;
        }
    }

    // Prüft, ob ein Neustart läuft
    public boolean isRunning() {
        return currentTimer != null && !currentTimer.isCancelled();
    }
}
