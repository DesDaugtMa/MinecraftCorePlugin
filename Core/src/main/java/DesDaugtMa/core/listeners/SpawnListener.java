package DesDaugtMa.core.listeners;

import DesDaugtMa.core.manager.SpawnManager;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class SpawnListener implements Listener {

    private final SpawnManager spawnManager;

    public SpawnListener(SpawnManager spawnManager) {
        this.spawnManager = spawnManager;
    }

    // Fall 1: Spieler joint zum allerersten Mal
    @EventHandler
    public void onFirstJoin(PlayerJoinEvent event) {
        // Prüfen, ob Spieler schon mal da war
        if (!event.getPlayer().hasPlayedBefore()) {
            Location spawn = spawnManager.getSpawn();

            // Nur teleportieren, wenn ein Spawn gesetzt ist
            if (spawn != null) {
                event.getPlayer().teleport(spawn);
            }
        }
    }

    // Fall 2: Spieler stirbt
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        // Zuerst prüfen: Ist das ein Bett-Spawn oder Seelenanker-Spawn?
        // Wenn ja -> Nichts tun, Minecraft-Standard lassen.
        if (event.isBedSpawn() || event.isAnchorSpawn()) {
            return;
        }

        // Wenn wir hier sind, hat der Spieler KEIN Bett/Anker oder es wurde zerstört.
        // -> Wir überschreiben das Respawn-Ziel mit unserem Spawn.
        Location spawn = spawnManager.getSpawn();

        if (spawn != null) {
            event.setRespawnLocation(spawn);
        }
    }
}
