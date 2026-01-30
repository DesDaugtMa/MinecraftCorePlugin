package DesDaugtMa.core;

import DesDaugtMa.core.commands.CoreCommand;
import DesDaugtMa.core.commands.SpawnCommand;
import DesDaugtMa.core.listeners.SpawnListener;
import DesDaugtMa.core.listeners.StopOverrideListener;
import DesDaugtMa.core.manager.MessageManager;
import DesDaugtMa.core.manager.SpawnManager;
import DesDaugtMa.core.tasks.TPSUtil;
import DesDaugtMa.core.tasks.TablistUpdater;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Hauptklasse des Core-Plugins.
 * Verantwortlich für das Bootstrapping der Manager, Listener und Tasks.
 */
public final class Core extends JavaPlugin {

    private MessageManager messageManager;
    private SpawnManager spawnManager;

    @Override
    public void onEnable() {
        // Konfiguration initialisieren
        saveDefaultConfig();

        // Manager initialisieren (Dependency Injection Vorbereitung)
        this.messageManager = new MessageManager(this);
        this.spawnManager = new SpawnManager(this);

        // Hintergrund-Tasks starten
        new TPSUtil().runTaskTimer(this, 0L, 20L);
        new TablistUpdater(this).runTaskTimer(this, 0L, 20L);

        // Commands registrieren
        registerCommands();

        // Listener registrieren
        registerListeners();

        getLogger().info("Core-System erfolgreich aktiviert.");
    }

    private void registerCommands() {
        getCommand("setspawn").setExecutor(new SpawnCommand(spawnManager, messageManager));
        getCommand("core").setExecutor(new CoreCommand(this, messageManager));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new SpawnListener(spawnManager), this);
        getServer().getPluginManager().registerEvents(new StopOverrideListener(messageManager), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Core-System deaktiviert.");
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }
}