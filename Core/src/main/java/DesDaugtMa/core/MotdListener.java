package DesDaugtMa.core;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class MotdListener implements Listener {

    private final MotdManager motdManager;

    public MotdListener(MotdManager motdManager) {
        this.motdManager = motdManager;
    }

    @EventHandler
    public void onServerPing(ServerListPingEvent event) {
        if (motdManager.isEnabled() && motdManager.getMotd() != null) {
            event.setMotd(motdManager.getMotd());
        }
    }
}
