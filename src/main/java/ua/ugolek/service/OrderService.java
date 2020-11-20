package ua.ugolek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ua.ugolek.model.Client;
import ua.ugolek.model.Order;
import ua.ugolek.model.OrderStatus;
import ua.ugolek.projection.OrdersCountProjection;
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
public class OrderService extends CrudService<Order> {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ClientService clientService;

    @Override
    public Order create(Order order) {
        String phoneNumber = order.getPhoneNumber();
        Client client = clientService.findByPhoneNumber(phoneNumber).orElseGet(() ->
                clientService.createClientFromOrderDetails(order.getClient()));
        order.setClient(client);
        order.setStatus(OrderStatus.PENDING);
        return orderRepository.save(order);
    }

    public Order createWithStatus(Order order, OrderStatus orderStatus) {
        order.setStatus(orderStatus);
        return orderRepository.save(order);
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

    @Override
    protected JpaRepository<Order, Long> getRepository() {
        return orderRepository;
    }

}
