package DesDaugtMa.core;

import org.bukkit.plugin.java.JavaPlugin;

public final class Core extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // 1. Manager erstellen
        MessageManager messageManager = new MessageManager(this);
        RestartManager restartManager = new RestartManager(this, messageManager);
        SpawnManager spawnManager = new SpawnManager(this);
        MotdManager motdManager = new MotdManager(this); // NEU

        // 2. Tasks
        new TPSUtil().runTaskTimer(this, 0L, 20L);
        new TablistUpdater().runTaskTimer(this, 0L, 20L);
        AutoRestartScheduler autoRestartScheduler = new AutoRestartScheduler(this, restartManager);
        autoRestartScheduler.runTaskTimer(this, 20L, 20L);

        // 3. Commands (CoreCommand bekommt jetzt auch den MotdManager!)
        getCommand("neustart").setExecutor(new RestartCommand(restartManager, messageManager));
        getCommand("setspawn").setExecutor(new SpawnCommand(spawnManager, messageManager));
        getCommand("core").setExecutor(new CoreCommand(this, messageManager, autoRestartScheduler, motdManager));

        // 4. Listeners
        getServer().getPluginManager().registerEvents(new SpawnListener(spawnManager), this);
        getServer().getPluginManager().registerEvents(new StopOverrideListener(messageManager), this);
        getServer().getPluginManager().registerEvents(new MotdListener(motdManager), this); // NEU

        getLogger().info("Core wurde aktiviert!");
    }

    @Override
    public void onDisable() {
        getLogger().info("SimpleTabList wurde deaktiviert.");
    }
}
