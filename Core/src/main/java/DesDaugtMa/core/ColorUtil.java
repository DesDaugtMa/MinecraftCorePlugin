package DesDaugtMa.core;

import net.md_5.bungee.api.ChatColor;
import java.awt.Color;

public class ColorUtil {

    /**
     * Erzeugt einen Farbverlauf mit optionalen Stilen.
     * @param strikethrough Soll der Text durchgestrichen sein?
     * @param bold Soll der Text fett sein?
     */
    public static String getGradient(String text, String start, String end, boolean strikethrough, boolean bold) {
        StringBuilder builder = new StringBuilder();

        Color startColor = Color.decode(start);
        Color endColor = Color.decode(end);

        int length = text.length();

        for (int i = 0; i < length; i++) {
            float ratio = (float) i / (float) (length - 1);

            int red = (int) (startColor.getRed() + ratio * (endColor.getRed() - startColor.getRed()));
            int green = (int) (startColor.getGreen() + ratio * (endColor.getGreen() - startColor.getGreen()));
            int blue = (int) (startColor.getBlue() + ratio * (endColor.getBlue() - startColor.getBlue()));

            ChatColor color = ChatColor.of(new Color(red, green, blue));

            // 1. Farbe setzen
            builder.append(color);

            // 2. Stile hinzufügen (Reihenfolge ist egal, aber muss nach Farbe kommen)
            if (strikethrough) {
                builder.append(ChatColor.STRIKETHROUGH);
            }
            if (bold) {
                builder.append(ChatColor.BOLD);
            }

            // 3. Buchstaben anfügen
            builder.append(text.charAt(i));
        }

        return builder.toString();
    }
}
