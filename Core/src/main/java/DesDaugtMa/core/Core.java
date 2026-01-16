package DesDaugtMa.core;

import org.bukkit.plugin.java.JavaPlugin;

public final class Core extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        MessageManager messageManager = new MessageManager(this);
        RestartManager restartManager = new RestartManager(this, messageManager);
        SpawnManager spawnManager = new SpawnManager(this);

        new TPSUtil().runTaskTimer(this, 0L, 20L);
        new TablistUpdater().runTaskTimer(this, 0L, 20L);

        // HIER GEÄNDERT: Scheduler in Variable speichern
        AutoRestartScheduler autoRestartScheduler = new AutoRestartScheduler(this, restartManager);
        autoRestartScheduler.runTaskTimer(this, 20L, 20L);

        // Commands registrieren
        getCommand("neustart").setExecutor(new RestartCommand(restartManager, messageManager));
        getCommand("setspawn").setExecutor(new SpawnCommand(spawnManager, messageManager));

        // HIER NEU: Core Command registrieren und Scheduler übergeben
        getCommand("core").setExecutor(new CoreCommand(this, messageManager, autoRestartScheduler));

        getServer().getPluginManager().registerEvents(new SpawnListener(spawnManager), this);
        getServer().getPluginManager().registerEvents(new StopOverrideListener(messageManager), this);

        getLogger().info("Core wurde aktiviert!");
    }

    @Override
    public void onDisable() {
        getLogger().info("SimpleTabList wurde deaktiviert.");
    }
}
