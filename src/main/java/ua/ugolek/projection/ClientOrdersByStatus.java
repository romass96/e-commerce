package ua.ugolek.projection;

import ua.ugolek.model.OrderStatus;

public interface ClientOrdersByStatus
{
    OrderStatus getOrderStatus();
    Long getOrderCount();
}
