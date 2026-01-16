package DesDaugtMa.core;

import org.bukkit.scheduler.BukkitRunnable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AutoRestartScheduler extends BukkitRunnable {

    private final RestartManager restartManager;
    private final String targetTimeStr;
    private final int countdownSeconds;
    private final boolean enabled;

    public AutoRestartScheduler(Core plugin, RestartManager manager) {
        this.restartManager = manager;

        // Werte aus der Config lesen
        this.enabled = plugin.getConfig().getBoolean("auto-restart.enabled");
        this.targetTimeStr = plugin.getConfig().getString("auto-restart.time", "04:00");
        this.countdownSeconds = plugin.getConfig().getInt("auto-restart.countdown-seconds", 300);
    }

    @Override
    public void run() {
        if (!enabled) return;

        // Aktuelle Uhrzeit holen
        LocalTime now = LocalTime.now();

        // Wir formatieren die aktuelle Zeit zu "HH:mm" (z.B. "04:00")
        String currentTimeStr = now.format(DateTimeFormatter.ofPattern("HH:mm"));

        // Prüfen: Ist es die richtige Zeit? UND läuft NICHT schon ein Timer?
        // Wichtig: Wir prüfen "!restartManager.isRunning()", damit wir nicht
        // jede Sekunde um 04:00 einen neuen Timer starten, sondern nur einmal.
        if (currentTimeStr.equals(targetTimeStr) && !restartManager.isRunning()) {
            restartManager.startRestart(countdownSeconds);
        }
    }
}
