package ua.ugolek.projection;

import java.time.LocalDate;

public interface OrdersCountProjection {
    LocalDate getCreatedDate();
    Long getOrdersCount();
}
