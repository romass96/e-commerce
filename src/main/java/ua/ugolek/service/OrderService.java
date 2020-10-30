package ua.ugolek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.ugolek.exception.ObjectNotFoundException;
import ua.ugolek.model.Order;
import ua.ugolek.model.OrderStatus;
import ua.ugolek.payload.filters.OrderFilter;
import ua.ugolek.projection.OrdersCountProjection;
import ua.ugolek.repository.AdvancedOrderRepository;
import ua.ugolek.repository.OrderRepository;
import ua.ugolek.util.DateUtils;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService extends FilterSupportService<Order, OrderFilter> {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    public OrderService(AdvancedOrderRepository filterSupportRepository) {
        super(filterSupportRepository);
    }

    public Order create(Order order) {
        order.setStatus(OrderStatus.PENDING);
        return orderRepository.save(order);
    }

    public Order update(Order order) {
        return orderRepository.save(order);
    }

    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    public Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(Order.class.getSimpleName(), id));
    }

    public void cancelOrder(Long orderId) {
        Order order = getById(orderId);
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
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
