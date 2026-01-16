package DesDaugtMa.core;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RestartTimer extends BukkitRunnable {

    private int secondsLeft;
    private final MessageManager msg; // Kurzform

    public RestartTimer(int seconds, MessageManager messageManager) {
        this.secondsLeft = seconds;
        this.msg = messageManager;
    }

    @Override
    public void run() {
        if (secondsLeft <= 0) {
            msg.broadcast("restart-broadcast-now"); // Config Key
            Bukkit.shutdown();
            cancel();
            return;
        }

        String timeString = formatTime(secondsLeft);

        // Actionbar an alle senden (Config Key: restart-actionbar)
        for (Player player : Bukkit.getOnlinePlayers()) {
            msg.sendActionBar(player, "restart-actionbar", "%time%", timeString);

            if (isImportantTime(secondsLeft)) {
                // Titel senden (Config Keys: restart-title, restart-subtitle)
                msg.sendTitle(player, "restart-title", "restart-subtitle", "%time%", timeString);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
            }
        }

        secondsLeft--;
    }

    private boolean isImportantTime(int s) {
        return s == 600 || s == 300 || s == 60 || s == 30 || s == 10 || s <= 5;
    }

    private String formatTime(int totalSeconds) {
        // ... (Deine bestehende Logik für Zeitformatierung bleibt hier gleich) ...
        // Um Platz zu sparen, kopiere hier deine bestehende formatTime Methode rein.
        // Falls du sie nicht mehr hast, sag Bescheid.
        int hours = totalSeconds / 3600;
        int remainder = totalSeconds % 3600;
        int minutes = remainder / 60;
        int seconds = remainder % 60;
        StringBuilder sb = new StringBuilder();
        if (hours > 0) { sb.append(hours).append(" "); sb.append(hours == 1 ? "Stunde" : "Stunden"); }
        if (minutes > 0 || (hours > 0 && seconds > 0)) { if (sb.length() > 0) sb.append(" "); sb.append(minutes).append(" "); sb.append(minutes == 1 ? "Minute" : "Minuten"); }
        if (seconds > 0 || (hours == 0 && minutes == 0)) { if (sb.length() > 0) sb.append(" "); sb.append(seconds).append(" "); sb.append(seconds == 1 ? "Sekunde" : "Sekunden"); }
        return sb.toString();
    }
}
