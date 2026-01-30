package DesDaugtMa.core.util;

import java.text.NumberFormat;
import java.util.Locale;

public class TimeUtil {

    private static final NumberFormat numberFormat = NumberFormat.getInstance(Locale.GERMAN);

    public static String formatPlayTime(long ticks) {
        long hours = (ticks / 20) / 3600;
        return numberFormat.format(hours) + " Stunden";
    }
}
