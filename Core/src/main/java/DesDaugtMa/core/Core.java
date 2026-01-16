package DesDaugtMa.core;

import org.bukkit.plugin.java.JavaPlugin;

public final class Core extends JavaPlugin {

    @Override
    public void onEnable() {
        // 1. Config laden (erstellt config.yml, falls nicht vorhanden)
        saveDefaultConfig();

        // 2. Manager initialisieren
        RestartManager restartManager = new RestartManager(this);

        // 3. Tasks starten
        new TPSUtil().runTaskTimer(this, 0L, 20L);
        new TablistUpdater().runTaskTimer(this, 0L, 20L);

        // Der AutoRestartScheduler prüft jede Sekunde (20 Ticks) die Uhrzeit
        // Man könnte auch 1200L (60 Sek) nehmen, aber 20L ist präziser beim Serverstart
        new AutoRestartScheduler(this, restartManager).runTaskTimer(this, 20L, 20L);

        // 4. Command registrieren (Wir übergeben den Manager!)
        getCommand("neustart").setExecutor(new RestartCommand(restartManager));

        getLogger().info("SimpleTabList wurde aktiviert!");
    }

    @Override
    public void onDisable() {
        getLogger().info("SimpleTabList wurde deaktiviert.");
    }
}
