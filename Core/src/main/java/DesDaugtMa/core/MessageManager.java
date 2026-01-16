package DesDaugtMa.core;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MessageManager {

    private final JavaPlugin plugin;

    public MessageManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Sendet eine Nachricht an einen Sender (Spieler oder Konsole).
     */
    public void send(CommandSender sender, String key, String... placeholders) {
        String text = getRawMessage(key, placeholders);
        if (text != null && !text.isEmpty()) {
            sender.sendMessage(text);
        }
    }

    /**
     * Sendet eine Nachricht an alle Spieler (Broadcast).
     */
    public void broadcast(String key, String... placeholders) {
        String text = getRawMessage(key, placeholders);
        if (text != null && !text.isEmpty()) {
            Bukkit.broadcastMessage(text);
        }
    }

    /**
     * Sendet eine Actionbar an einen Spieler.
     */
    public void sendActionBar(Player player, String key, String... placeholders) {
        String text = getRawMessage(key, placeholders);
        if (text != null && !text.isEmpty()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(text));
        }
    }

    /**
     * Sendet Titel und Untertitel.
     */
    public void sendTitle(Player player, String titleKey, String subtitleKey, String... placeholders) {
        String title = getRawMessage(titleKey, placeholders);
        String subtitle = getRawMessage(subtitleKey, placeholders);

        // Wenn beide deaktiviert sind, senden wir nichts
        if (title == null && subtitle == null) return;

        // Falls einer null ist (deaktiviert), machen wir ihn leer für die API
        if (title == null) title = "";
        if (subtitle == null) subtitle = "";

        player.sendTitle(title, subtitle, 10, 70, 20);
    }

    /**
     * Private Hilfsmethode zum Laden und Formatieren.
     * Gibt NULL zurück, wenn enabled: false
     */
    private String getRawMessage(String key, String... placeholders) {
        String path = "messages." + key;

        // Prüfen ob enabled
        if (!plugin.getConfig().getBoolean(path + ".enabled", true)) {
            return null;
        }

        String text = plugin.getConfig().getString(path + ".text");
        if (text == null) return null;

        // Prefix holen
        String prefix = plugin.getConfig().getString("prefix", "&7[System] ");
        text = text.replace("%prefix%", prefix);

        // Platzhalter ersetzen (Paarweise: "%key%", "value")
        for (int i = 0; i < placeholders.length; i += 2) {
            if (i + 1 < placeholders.length) {
                String phKey = placeholders[i]; // z.B. "%time%"
                String phVal = placeholders[i+1]; // z.B. "10 Sekunden"
                text = text.replace(phKey, phVal);
            }
        }

        // Farben übersetzen (& -> §)
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
