package DesDaugtMa.core;

import org.bukkit.plugin.java.JavaPlugin;

public final class Core extends JavaPlugin {

    @Override
    public void onEnable() {
        // 1. Starte den TPS Rechner
        // Wir lassen ihn alle 20 Ticks (ca. 1 Sekunde) laufen, um zu messen
        new TPSUtil().runTaskTimer(this, 0L, 20L);

        // 2. Starte den TabList Updater
        // Alle 20 Ticks (1 Sekunde) reicht für die Anzeige
        new TablistUpdater().runTaskTimer(this, 0L, 20L);

        getLogger().info("SimpleTabList wurde aktiviert!");
    }

    @Override
    public void onDisable() {
        getLogger().info("SimpleTabList wurde deaktiviert.");
    }
}
