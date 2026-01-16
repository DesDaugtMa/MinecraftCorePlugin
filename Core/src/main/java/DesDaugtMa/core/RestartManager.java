package DesDaugtMa.core;

import org.bukkit.plugin.java.JavaPlugin;

public class RestartManager {

    private final JavaPlugin plugin;
    private final MessageManager messageManager; // Referenz
    private RestartTimer currentTimer;

    // Konstruktor geändert!
    public RestartManager(JavaPlugin plugin, MessageManager messageManager) {
        this.plugin = plugin;
        this.messageManager = messageManager;
    }

    public void startRestart(int seconds) {
        if (isRunning()) {
            currentTimer.cancel();
        }
        // Wir übergeben den MessageManager auch an den Timer!
        currentTimer = new RestartTimer(seconds, messageManager);
        currentTimer.runTaskTimer(plugin, 0L, 20L);
    }

    public void cancelRestart() {
        if (isRunning()) {
            currentTimer.cancel();
            currentTimer = null;
        }
    }

    public boolean isRunning() {
        return currentTimer != null && !currentTimer.isCancelled();
    }
}
