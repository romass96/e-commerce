package ua.ugolek.util;

import ua.ugolek.model.Order;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Comparators {
    private static final Map<String, Comparator<Order>> orderComparators = new HashMap<>();

    static {
        orderComparators.put("id", Comparator.comparing(Order::getId));
        orderComparators.put("client", Comparator.comparing(order -> order.getClient().getFullName()));
        orderComparators.put("totalOrderPrice", Comparator.comparing(Order::getTotalOrderPrice));
    }

    public static Comparator<Order> getOrderComparatorByCode(String code, boolean sortDesc) {
        return reverseIfNeeded(orderComparators.get(code), sortDesc);
    }

    public static <T> Comparator<T> reverseIfNeeded(Comparator<T> source, boolean sortDesc) {
        if (sortDesc) {
            return source.reversed();
        }
        return source;
    }
}
