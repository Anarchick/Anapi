package fr.anarchick.anapi.bukkit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtils {

    public final long xDays, xHours, xMinutes, xSeconds, xTicks;
    public final long days, hours, minutes, seconds, ticks;

    public TimeUtils(long ticks) {
        this.xTicks = ticks;
        this.xSeconds = ticks / 20;
        this.xMinutes = ticks / 1200;
        this.xHours = ticks / 72000;
        this.xDays = ticks / 1728000;

        this.days = xDays;
        this.hours = xSeconds % 86400 / 3600;
        this.minutes = xSeconds % 3600 / 60;
        this.seconds = xSeconds % 60;
        this.ticks = ticks % 20;
    }

    @Override
    public String toString() {
        return format("d", "h", "m", "s", "t");
    }

    @Override
    public int hashCode() {
        return (int) xTicks;
    }

    /**
     * d    days
     * h    hours
     * m    minutes
     * s    seconds
     * t    ticks
     *
     * H    total of hours
     * M    total of minutes
     * S    total of seconds
     * T    total of ticks
     *
     * Note : Use regex "(^0+| +0+) ?[a-zA-Z]+ ?" to replace zero values in some situations
     *
     * @param input e.g "%h% hours %m% minutes %s% seconds% or "%hh%:%mm% %ss%"
     * @return
     */
    public @Nonnull String format(@Nonnull String input) {
        String[] placeholders = new String[] {"d", "h", "m", "s", "t", "H", "M", "S", "T"};
        for (String placeholder : placeholders) {
            long value;
            switch (placeholder) {
                case "d" -> value = days;
                case "h" -> value = hours;
                case "m" -> value = minutes;
                case "s" -> value = seconds;
                case "t" -> value = ticks;
                case "H" -> value = xHours;
                case "M" -> value = xMinutes;
                case "S" -> value = xSeconds;
                case "T" -> value = xTicks;
                default -> value = 0;
            }
            Pattern pattern = Pattern.compile("(%"+placeholder+"+%)");
            Matcher matcher = pattern.matcher(input);
            while (matcher.find()) {
                String group = matcher.group(1);
                int length = group.length() - 2;
                String replacement = String.format("%0"+length+"d", value);
                input = input.replaceFirst(group, replacement);
                matcher = pattern.matcher(input);
            }
        }
        return input;
    }

    /**
     * Format to "%d%daysName %h%hoursName %m%minutesName %s%secondsName %t%ticksName"
     * and replace zero values
     * @param daysName
     * @param hoursName
     * @param minutesName
     * @param secondsName
     * @param ticksName
     * @return
     */
    public @Nonnull String format(@Nullable String daysName, @Nullable String hoursName, @Nullable String minutesName, @Nullable String secondsName, @Nullable String ticksName) {
        if (xTicks == 0) return format("%t%" + ticksName);
        StringBuilder sb = new StringBuilder();
        if (days > 0 && daysName != null) sb.append("%d%").append(daysName).append(" ");
        if (hours > 0 && hoursName != null) sb.append("%h%").append(hoursName).append(" ");
        if (minutes > 0 && minutesName != null) sb.append("%m%").append(minutesName).append(" ");
        if(seconds > 0 && secondsName != null) sb.append("%s%").append(secondsName).append(" ");
        if (ticks > 0 && ticksName != null) sb.append("%t%").append(ticksName).append(" ");
        sb.replace(sb.length()-1, sb.length(), "");
        return format(sb.toString());
    }

}
