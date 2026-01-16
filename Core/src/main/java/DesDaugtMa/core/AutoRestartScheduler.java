package DesDaugtMa.core;

import org.bukkit.scheduler.BukkitRunnable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AutoRestartScheduler extends BukkitRunnable {

    private final Core plugin; // Referenz speichern
    private final RestartManager restartManager;

    // Diese Werte müssen beim Reload aktualisiert werden
    private String targetTimeStr;
    private int countdownSeconds;
    private boolean enabled;

    public AutoRestartScheduler(Core plugin, RestartManager manager) {
        this.plugin = plugin;
        this.restartManager = manager;

        // Werte initial laden
        loadSettings();
    }

    // NEUE METHODE: Lädt die Werte neu aus der Config
    public void loadSettings() {
        this.enabled = plugin.getConfig().getBoolean("auto-restart.enabled");
        this.targetTimeStr = plugin.getConfig().getString("auto-restart.time", "04:00");
        this.countdownSeconds = plugin.getConfig().getInt("auto-restart.countdown-seconds", 300);
    }

    @Override
    public void run() {
        if (!enabled) return;

        LocalTime now = LocalTime.now();
        String currentTimeStr = now.format(DateTimeFormatter.ofPattern("HH:mm"));

        if (currentTimeStr.equals(targetTimeStr) && !restartManager.isRunning()) {
            restartManager.startRestart(countdownSeconds);
        }
    }
}
