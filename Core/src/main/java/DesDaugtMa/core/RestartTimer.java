package DesDaugtMa.core;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RestartTimer extends BukkitRunnable {

    private int secondsLeft;

    public RestartTimer(int seconds) {
        this.secondsLeft = seconds;
    }

    @Override
    public void run() {
        if (secondsLeft <= 0) {
            Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "Der Server startet nun neu!");
            Bukkit.shutdown();
            cancel();
            return;
        }

        // --- Actionbar Text Bauen ---
        String timeString = formatTime(secondsLeft);

        // HIER GEÄNDERT:
        // Rot + Fett für "Neustart"
        // Reset + Grau für " in "
        // Gelb für die Zeit
        String actionBarMessage = ChatColor.RED + "" + ChatColor.BOLD + "Neustart " +
                ChatColor.RESET + ChatColor.GRAY + "in " +
                ChatColor.YELLOW + timeString;

        TextComponent actionComponent = new TextComponent(actionBarMessage);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, actionComponent);

            if (isImportantTime(secondsLeft)) {
                String title = ChatColor.RED + "" + ChatColor.BOLD + "Neustart";
                String subtitle = ChatColor.GRAY + "Neustart in " + ChatColor.YELLOW + timeString;

                player.sendTitle(title, subtitle, 0, 40, 10);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
            }
        }

        secondsLeft--;
    }

    private boolean isImportantTime(int s) {
        return s == 600 || s == 300 || s == 60 || s == 30 || s == 10 || s <= 5;
    }

    private String formatTime(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int remainder = totalSeconds % 3600;
        int minutes = remainder / 60;
        int seconds = remainder % 60;

        StringBuilder sb = new StringBuilder();

        if (hours > 0) {
            sb.append(hours).append(" ");
            sb.append(hours == 1 ? "Stunde" : "Stunden");
        }

        if (minutes > 0 || (hours > 0 && seconds > 0)) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(minutes).append(" ");
            sb.append(minutes == 1 ? "Minute" : "Minuten");
        }

        if (seconds > 0 || (hours == 0 && minutes == 0)) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(seconds).append(" ");
            sb.append(seconds == 1 ? "Sekunde" : "Sekunden");
        }

        return sb.toString();
    }
}
