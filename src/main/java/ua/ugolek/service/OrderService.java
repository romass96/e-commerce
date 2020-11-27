package ua.ugolek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ua.ugolek.model.Client;
import ua.ugolek.model.Order;
import ua.ugolek.model.OrderItem;
import ua.ugolek.model.OrderStatus;
import ua.ugolek.projection.ClientOrdersByStatus;
import ua.ugolek.projection.OrdersCountProjection;
import ua.ugolek.repository.OrderRepository;
import ua.ugolek.util.DateUtils;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Validated
public class OrderService extends CRUDService<Order>
{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ProductService productService;

    @Override
    public Order create(@Valid Order order) {
        String phoneNumber = order.getPhoneNumber();
        Client client = clientService.findByPhoneNumber(phoneNumber).orElseGet(() ->
                clientService.createClientFromOrderDetails(order.getClient()));
        order.setClient(client);
        order.setStatus(OrderStatus.PENDING);
        order.getOrderItems().forEach(this::allocateProductsForOrderItem);
        return orderRepository.save(order);
    }

    private void checkOrderItemsQuantityIsAvailableInStore(Order order) {

    }

    public Order createWithStatus(Order order, OrderStatus orderStatus) {
        order.setStatus(orderStatus);
        return orderRepository.save(order);
    }

    public void cancelOrder(Long orderId) {
        Order order = getById(orderId);
        order.getOrderItems().forEach(this::freeUpProductsFromCancelledOrderItem);

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);


    }

    private void allocateProductsForOrderItem(OrderItem orderItem) {

    }

    private void freeUpProductsFromCancelledOrderItem(OrderItem orderItem) {

    }

    public Map<LocalDate, Long> countOrdersByCreatedDate(String periodCode) {
        Period period = DateUtils.getPeriodByCode(periodCode);
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minus(period);
        List<OrdersCountProjection> items = orderRepository.countOrdersByCreatedDate(startDate);

        CountByDateStatistics<OrdersCountProjection> statistics = new CountByDateStatistics<>();
        statistics.setCountMapper(OrdersCountProjection::getOrdersCount);
        statistics.setDateMapper(OrdersCountProjection::getCreatedDate);
        statistics.setStartDate(startDate.toLocalDate());
        statistics.setEndDate(endDate.toLocalDate());

        return statistics.getEveryDayData(items);
    }

    public List<ClientOrdersByStatus> countOrdersByStatusForClient(Long clientId) {
        return orderRepository.countOrdersByStatusForClient(clientId);
    }

    @Override
    protected JpaRepository<Order, Long> getRepository() {
        return orderRepository;
    }

}
