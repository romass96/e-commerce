package ua.ugolek.controller.api;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.ugolek.dto.OrderDTO;
import ua.ugolek.model.Order;
import ua.ugolek.payload.ListResponse;
import ua.ugolek.payload.filters.OrderFilter;
import ua.ugolek.projection.ClientOrdersByStatus;
import ua.ugolek.projection.ClientProductsByCategories;
import ua.ugolek.service.OrderService;
import ua.ugolek.service.dto.OrderDTOService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDTOService orderDTOService;

    @GetMapping("")
    public List<Order> getAllOrders() {
        return orderService.getAll();
    }

    @PostMapping("")
    public HttpStatus createOrder(@RequestBody Order order) {
        orderService.create(order);
        return HttpStatus.CREATED;
    }

    @PostMapping("/filter")
    public ListResponse<OrderDTO> getOrdersByFilter(@RequestBody OrderFilter filter) {
        return orderDTOService.queryByFilter(filter);
    }

    @GetMapping("/orderStatistics")
    public Map<LocalDate, Long> getOrderStatistics(@RequestParam String period) {
        return orderService.countOrdersByCreatedDate(period);
    }

    @GetMapping("/orderDetails")
    public Order getOrderDetails(@RequestParam Long id) {
        return orderService.getById(id);
    }

    @PostMapping("/cancelOrder")
    @ResponseStatus(HttpStatus.OK)
    public void cancelOrder(@RequestBody JsonNode requestBody) {
        orderService.cancelOrder(requestBody.get("orderId").asLong());
    }

    @GetMapping("/clientOrdersForStatuses")
    public List<ClientOrdersByStatus> countOrdersByStatusForClient(@RequestParam Long clientId) {
        return orderService.countOrdersByStatusForClient(clientId);
    }
}
