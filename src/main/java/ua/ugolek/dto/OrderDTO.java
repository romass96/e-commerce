package ua.ugolek.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ua.ugolek.model.OrderItem;
import ua.ugolek.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderDTO implements DTO {
    private Long id;
    private OrderStatus status;
//    private boolean paid;
    private String clientFullName;
    private Long clientId;
    private String phoneNumber;
    private double totalOrderPrice;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime createdDate;
    private List<OrderItem> orderItems;
}
