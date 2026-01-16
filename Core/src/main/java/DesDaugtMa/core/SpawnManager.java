package DesDaugtMa.core;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class SpawnManager {

    private final JavaPlugin plugin;

    public SpawnManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Speichert die Location in die Config.
     */
    public void setSpawn(Location location) {
        FileConfiguration config = plugin.getConfig();

        config.set("spawn.world", location.getWorld().getName());
        config.set("spawn.x", location.getX());
        config.set("spawn.y", location.getY());
        config.set("spawn.z", location.getZ());
        config.set("spawn.yaw", location.getYaw());
        config.set("spawn.pitch", location.getPitch());

        // Wichtig: Config speichern, sonst sind die Daten nach Neustart weg
        plugin.saveConfig();
    }

    /**
     * Lädt den Spawn aus der Config.
     * @return Location oder null, wenn kein Spawn gesetzt wurde.
     */
    public Location getSpawn() {
        FileConfiguration config = plugin.getConfig();

        // Prüfen, ob überhaupt ein Spawn gesetzt ist
        if (!config.contains("spawn.world")) {
            return null;
        }

        String worldName = config.getString("spawn.world");
        World world = Bukkit.getWorld(worldName);

        // Falls die Welt nicht existiert (z.B. gelöscht wurde), geben wir null zurück
        if (world == null) {
            return null;
        }

        double x = config.getDouble("spawn.x");
        double y = config.getDouble("spawn.y");
        double z = config.getDouble("spawn.z");
        // Float Cast ist wichtig, da Config doubles liefert, Location aber floats nutzt
        float yaw = (float) config.getDouble("spawn.yaw");
        float pitch = (float) config.getDouble("spawn.pitch");

        return new Location(world, x, y, z, yaw, pitch);
    }
}
