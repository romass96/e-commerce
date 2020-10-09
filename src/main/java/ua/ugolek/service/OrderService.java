package ua.ugolek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.ugolek.model.Order;
import ua.ugolek.model.OrderStatus;
import ua.ugolek.payload.OrderFilter;
import ua.ugolek.payload.OrderListResponse;
import ua.ugolek.projection.OrdersCountProjection;
import ua.ugolek.repository.OrderRepository;
import ua.ugolek.util.Comparators;
import ua.ugolek.util.DateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order create(Order order) {
        order.setStatus(OrderStatus.PENDING);
        return orderRepository.save(order);
    }

    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    public void cancelOrder(Long orderId) {
        orderRepository.findById(orderId).ifPresent(order -> {
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
        });
    }

    public List<Order> findCancelledOrders() {
        return orderRepository.findByStatus(OrderStatus.CANCELLED);
    }

    public List<Order> findCompletedOrders() {
        return orderRepository.findByStatus(OrderStatus.COMPLETED);
    }

    public OrderListResponse queryByFilter(OrderFilter filter) {
        Pageable pageable = PageRequest.of(filter.getPageNumber(), filter.getPerPage());
        Page<Order> orderPage;
        Long categoryId = filter.getCategoryId();
        if (categoryId == null) {
            orderPage = orderRepository.filter(pageable, filter.getStatus());
        } else {
            orderPage = orderRepository.filter(pageable, categoryId, filter.getStatus());
        }
//        String sortBy = filter.getSortBy();
//        if (sortBy != null) {
//            orderPage.getContent().sort(Comparators.getOrderComparatorByCode(sortBy, filter.isSortDesc()));
//        }

        OrderListResponse response = new OrderListResponse();
        response.setOrders(orderPage.getContent());
        response.setTotalItems(orderPage.getTotalElements());
        response.setTotalPages(orderPage.getTotalPages());
        return response;
    }

    public Map<LocalDate, Long> countOrdersByCreatedDate(String periodCode) {
        Period period = DateUtils.getPeriodByCode(periodCode);
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minus(period);
        List<OrdersCountProjection> statistics = orderRepository.countOrdersByCreatedDate(startDate);
        Map<LocalDate, Long> map = statistics.stream().collect(Collectors.toMap(
                OrdersCountProjection::getCreatedDate,
                OrdersCountProjection::getOrdersCount, (prev, next) -> next, TreeMap::new));
        startDate.toLocalDate().datesUntil(endDate.toLocalDate()).forEach(date -> map.putIfAbsent(date, 0L));

        return map;

    }

}
