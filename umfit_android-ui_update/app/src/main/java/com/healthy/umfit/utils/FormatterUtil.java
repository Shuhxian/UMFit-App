package com.healthy.umfit.utils;

import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.ocpsoft.prettytime.PrettyTime;

import java.text.DecimalFormat;
import java.util.Date;

public class FormatterUtil {

    public static String formatPhoneNumber(String phone) {
        String formatted = phone.replaceAll("[^\\d]", "");
        if (formatted.startsWith("01")) {
            formatted = "+6" + formatted;
        } else if (formatted.startsWith("60")) {
            formatted = "+" + formatted;
        }
        return formatted;
    }

    public static String formatSmsCode(String code) {
        String formatted = code.replaceAll("[^\\d]", "");
        return formatted;
    }

    public static String formatDuration(long milis) {
        Duration duration = new Duration(milis); // in milliseconds
        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendDays()
                .appendSuffix("d")
                .appendHours()
                .appendSuffix("h")
                .appendMinutes()
                .appendSuffix("m")
                .appendSeconds()
                .appendSuffix("s")
                .toFormatter();
        String formatted = formatter.print(duration.toPeriod());
        return formatted;
    }

    public static String formatCurrency(long cents) {
        Double d = cents / 100D;
        DecimalFormat formatter = new DecimalFormat("###,###,##0.00");

        return formatter.format(d);
    }

    public static String formatDateElapsed(Date forDate) {
        return new PrettyTime().format(forDate);
    }

}
