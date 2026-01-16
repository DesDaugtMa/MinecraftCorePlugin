package DesDaugtMa.core;

import org.bukkit.plugin.java.JavaPlugin;

public final class Core extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // --- Manager ---
        RestartManager restartManager = new RestartManager(this);
        SpawnManager spawnManager = new SpawnManager(this); // NEU

        // --- Tasks ---
        new TPSUtil().runTaskTimer(this, 0L, 20L);
        new TablistUpdater().runTaskTimer(this, 0L, 20L);
        new AutoRestartScheduler(this, restartManager).runTaskTimer(this, 20L, 20L);

        // --- Commands ---
        getCommand("neustart").setExecutor(new RestartCommand(restartManager));
        getCommand("setspawn").setExecutor(new SpawnCommand(spawnManager)); // NEU

        // --- Listeners ---
        // Events müssen registriert werden, damit sie feuern
        getServer().getPluginManager().registerEvents(new SpawnListener(spawnManager), this); // NEU

        getLogger().info("Core (SimpleTabList) wurde aktiviert!");
    }

    @Override
    public void onDisable() {
        getLogger().info("SimpleTabList wurde deaktiviert.");
    }
}
