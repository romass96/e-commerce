package ua.ugolek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.ugolek.model.Order;
import ua.ugolek.model.OrderStatus;
import ua.ugolek.projection.OrdersCountProjection;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "SELECT DATE(o.created_date) as createdDate, COUNT(o) as ordersCount FROM orders o " +
            "WHERE o.created_date >= :startDate " +
            "GROUP BY createdDate " +
            "ORDER BY createdDate",
            nativeQuery = true)
    List<OrdersCountProjection> countOrdersByCreatedDate(@Param("startDate") LocalDateTime startDate);
}
