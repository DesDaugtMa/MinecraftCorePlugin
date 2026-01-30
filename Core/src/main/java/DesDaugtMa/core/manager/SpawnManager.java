package DesDaugtMa.core.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Verwaltet die Speicherung und den Abruf des globalen Spawnpunkts.
 */
public class SpawnManager {

    private final JavaPlugin plugin;

    public SpawnManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void setSpawn(Location location) {
        FileConfiguration config = plugin.getConfig();
        config.set("spawn.world", location.getWorld().getName());
        config.set("spawn.x", location.getX());
        config.set("spawn.y", location.getY());
        config.set("spawn.z", location.getZ());
        config.set("spawn.yaw", (double) location.getYaw());
        config.set("spawn.pitch", (double) location.getPitch());
        plugin.saveConfig();
    }

    public Location getSpawn() {
        FileConfiguration config = plugin.getConfig();
        if (!config.contains("spawn.world")) return null;

        World world = Bukkit.getWorld(config.getString("spawn.world"));
        if (world == null) return null;

        return new Location(
                world,
                config.getDouble("spawn.x"),
                config.getDouble("spawn.y"),
                config.getDouble("spawn.z"),
                (float) config.getDouble("spawn.yaw"),
                (float) config.getDouble("spawn.pitch")
        );
    }
}