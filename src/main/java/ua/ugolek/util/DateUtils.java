package ua.ugolek.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtils {
    private static final Map<String, Period> periodMap = new HashMap<>();

    static {
        periodMap.put("1W", Period.ofWeeks(1));
        periodMap.put("1M", Period.ofMonths(1));
        periodMap.put("3M", Period.ofMonths(3));
        periodMap.put("6M", Period.ofMonths(6));
        periodMap.put("1Y", Period.ofYears(1));
    }

    private DateUtils() {

    }

    public static Date getDate(int year, int month, int dayOfMonth) {
        return new Calendar.Builder().setDate(year,month,dayOfMonth).build().getTime();
    }

    public static LocalDateTime getInitialDate() {
        return LocalDateTime.of(1970, 1, 1, 0, 0);
    }

    public static Date subtractPeriodFromNow(Period period) {
        return subtractPeriodFrom(period, new Date());
    }

    public static Date subtractPeriodFrom(Period period, Date date) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).minus(period);
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Period getPeriodByCode(String periodCode) {
        return periodMap.getOrDefault(periodCode, Period.ofDays(1));
    }
}
