package ua.ugolek.payload;

import lombok.Getter;
import lombok.Setter;
import ua.ugolek.model.Order;

import java.util.List;

@Getter
@Setter
public class OrderListResponse {
    private long totalItems;
    private List<Order> orders;
    private int totalPages;
    private int currentPage;
}
