package DesDaugtMa.core.tasks;

import org.bukkit.scheduler.BukkitRunnable;

public class TPSUtil extends BukkitRunnable {

    private long lastTickTime;
    private static double currentTps = 20.0;

    public TPSUtil() {
        this.lastTickTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        long now = System.currentTimeMillis();
        long timeSpent = now - lastTickTime;

        // Wenn 0 Zeit vergangen ist (unwahrscheinlich), verhindern wir Division durch Null
        if (timeSpent < 1) {
            timeSpent = 1;
        }

        // Berechnung: Wir haben 20 Ticks gewartet.
        // Wie viele Sekunden sind dafür in "Echtzeit" vergangen?
        // Formel: 20 Ticks / (Vergangene Sekunden)
        double tps = 20.0 / (timeSpent / 1000.0);

        // Deckeln auf 20.0, falls der Server mal kurz aufholt (Lag-Spikes nach unten abfangen)
        if (tps > 20.0) {
            tps = 20.0;
        }

        currentTps = tps;
        lastTickTime = now;
    }

    // Statische Methode, damit wir von überall darauf zugreifen können
    public static double getTPS() {
        return currentTps;
    }
}
