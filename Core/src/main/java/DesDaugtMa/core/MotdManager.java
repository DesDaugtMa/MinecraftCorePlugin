package DesDaugtMa.core;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class MotdManager {

    private final JavaPlugin plugin;
    private String cachedMotd;
    private boolean enabled;

    // Ungefähre Breite einer Zeile in Minecraft Standard-Schriftart (Monospace-Annäherung)
    private static final int CENTER_PX = 60;

    public MotdManager(JavaPlugin plugin) {
        this.plugin = plugin;
        reload(); // Direkt beim Start laden
    }

    public void reload() {
        FileConfiguration config = plugin.getConfig();
        this.enabled = config.getBoolean("motd.enabled");
        boolean centered = config.getBoolean("motd.centered");

        if (!enabled) {
            return;
        }

        String line1 = config.getString("motd.line-1", "&cLine 1");
        String line2 = config.getString("motd.line-2", "&cLine 2");

        if (centered) {
            line1 = centerText(line1);
            line2 = centerText(line2);
        }

        // Farben übersetzen
        line1 = ChatColor.translateAlternateColorCodes('&', line1);
        line2 = ChatColor.translateAlternateColorCodes('&', line2);

        // Zusammenfügen
        this.cachedMotd = line1 + "\n" + line2;
    }

    public String getMotd() {
        return cachedMotd;
    }

    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Einfache Zentrierungs-Logik durch Padding mit Leerzeichen.
     */
    private String centerText(String text) {
        // Wir entfernen Farbcodes für die Längenberechnung
        String stripped = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', text));
        int length = stripped.length();

        if (length >= CENTER_PX) {
            return text; // Text ist zu lang zum Zentrieren
        }

        int padding = (CENTER_PX - length) / 2;
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < padding; i++) {
            builder.append(" ");
        }
        builder.append(text);

        return builder.toString();
    }
}
