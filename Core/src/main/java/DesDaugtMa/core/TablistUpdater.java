package DesDaugtMa.core;

import org.bukkit.Bukkit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;

public class TablistUpdater extends BukkitRunnable {

    // Der durchgestrichene Balken (Grau -> Gelb)
    private final String separatorLine = ColorUtil.getGradient("                                                ", "#D3D3D3", "#FFFF55", true, false);

    // Formatter für die TPS Anzeige (z.B. 19.95)
    private final DecimalFormat tpsFormat = new DecimalFormat("0.00");

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateTabList(player);
        }
    }

    private void updateTabList(Player player) {
        // --- Header Daten ---
        long timeInTicks = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        String formattedTime = TimeUtil.formatPlayTime(timeInTicks);

        // --- Footer Daten ---

        // 1. Ping holen
        int ping = player.getPing();

        // 2. TPS holen (Wir nehmen den 1-Minuten-Durchschnitt, Index 0)
        // Wir deckeln die Anzeige visuell auf 20.00, da Spigot manchmal Zahlen wie 20.000004 ausgibt.
        double tps = TPSUtil.getTPS();
        if (tps > 20.0) tps = 20.0;

        // 3. RAM berechnen (Runtime nutzt Bytes, wir wollen MB)
        long maxRam = Runtime.getRuntime().maxMemory() / (1024 * 1024);
        long usedRam = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);


        // --- Header bauen ---
        String header = separatorLine + ChatColor.RESET + "\n" +
                " \n" +
                ChatColor.GRAY + "Deine Spielzeit " + ChatColor.YELLOW + formattedTime + "\n" +
                " ";

        // --- Footer bauen ---
        // Zeile 1: Leer
        // Zeile 2: TPS [TPS]   Ping [Ping] (Ich nutze 3 Leerzeichen als Tab-Ersatz, da echte Tabs in MC oft verbuggen)
        // Zeile 3: RAM [Genutzt]/[Max]
        // Zeile 4: Leer
        // Zeile 5: Der Balken

        String footer = "\n" +
                ChatColor.GRAY + "TPS " + ChatColor.YELLOW + tpsFormat.format(tps) +
                "   " + // Das ist der manuelle "Tab" Abstand
                ChatColor.GRAY + "Ping " + ChatColor.YELLOW + ping + "ms" + "\n" +

                ChatColor.GRAY + "RAM " + ChatColor.YELLOW + usedRam + "MB" +
                ChatColor.GRAY + "/" +
                ChatColor.YELLOW + maxRam + "MB" + "\n" +

                " \n" +
                separatorLine; // Hier nutzen wir den Balken wieder

        // Alles setzen
        player.setPlayerListHeaderFooter(header, footer);
    }
}
