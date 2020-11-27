package ua.ugolek.service;

import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Setter
public class CountByDateStatistics<T>
{
    private LocalDate startDate;
    private LocalDate endDate;
    private Function<T, LocalDate> dateMapper;
    private Function<T, Long> countMapper;

    public Map<LocalDate, Long> getEveryDayData(List<T> items) {
        Map<LocalDate, Long> map = items.stream().collect(Collectors.toMap(
            dateMapper, countMapper, (prev, next) -> next, TreeMap::new));
        startDate.datesUntil(endDate).forEach(date -> map.putIfAbsent(date, 0L));
        return map;
    }
}
