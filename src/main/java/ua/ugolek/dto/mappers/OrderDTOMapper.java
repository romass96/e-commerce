package ua.ugolek.dto.mappers;

import org.springframework.stereotype.Component;
import ua.ugolek.dto.OrderDTO;
import ua.ugolek.model.Order;

import java.util.function.Function;

@Component
public class OrderDTOMapper implements Function<Order, OrderDTO> {
    @Override
    public OrderDTO apply(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .clientFullName(order.getClient().getFullName())
                .phoneNumber(order.getPhoneNumber())
                .clientId(order.getClient().getId())
                .createdDate(order.getCreatedDate())
                .status(order.getStatus())
                .totalOrderPrice(order.getTotalOrderPrice().doubleValue())
                .orderItems(order.getOrderItems())
                .build();
    }
}
