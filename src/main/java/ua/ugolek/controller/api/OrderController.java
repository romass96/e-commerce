package ua.ugolek.controller.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.ugolek.model.Order;
import ua.ugolek.payload.OrderFilter;
import ua.ugolek.payload.OrderListResponse;
import ua.ugolek.service.OrderService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("")
    public List<Order> getAllOrders() {
        return orderService.getAll();
    }

    @PostMapping("/filter")
    public OrderListResponse getOrdersByFilter(@RequestBody OrderFilter filter) {
        return orderService.queryByFilter(filter);
    }

    @GetMapping("/orderStatistics")
    public Map<LocalDate, Long> getOrderStatistics(@RequestParam String period) {
        return orderService.countOrdersByCreatedDate(period);
    }
}
