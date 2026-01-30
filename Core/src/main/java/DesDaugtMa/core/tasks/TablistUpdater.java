package DesDaugtMa.core.tasks;

import DesDaugtMa.core.util.ColorUtil;
import DesDaugtMa.core.util.TimeUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.text.DecimalFormat;

public class TablistUpdater extends BukkitRunnable {

    private final JavaPlugin plugin;
    private final DecimalFormat tpsFormat = new DecimalFormat("0.00");
    private final String separator;

    public TablistUpdater(JavaPlugin plugin) {
        this.plugin = plugin;
        this.separator = ColorUtil.getGradient("                                                ", "#D3D3D3", "#FFFF55", true, false);
    }

    @Override
    public void run() {
        double tps = Math.min(20.0, TPSUtil.getTPS());
        long maxRam = Runtime.getRuntime().maxMemory() / 1048576L;
        long usedRam = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576L;

        for (Player player : Bukkit.getOnlinePlayers()) {
            String time = TimeUtil.formatPlayTime(player.getStatistic(Statistic.PLAY_ONE_MINUTE));

            String header = separator + "\n \n" + ChatColor.GRAY + "Spielzeit: " + ChatColor.YELLOW + time + "\n ";
            String footer = "\n" + ChatColor.GRAY + "TPS: " + ChatColor.YELLOW + tpsFormat.format(tps) +
                    "  " + ChatColor.GRAY + "Ping: " + ChatColor.YELLOW + player.getPing() + "ms\n" +
                    ChatColor.GRAY + "RAM: " + ChatColor.YELLOW + usedRam + "MB / " + maxRam + "MB\n \n" + separator;

            player.setPlayerListHeaderFooter(header, footer);
        }
    }
}