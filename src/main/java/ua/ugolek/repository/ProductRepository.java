package ua.ugolek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.ugolek.model.OrderStatus;
import ua.ugolek.model.Product;
import ua.ugolek.projection.ProductSoldProjection;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p.name as productName, COUNT(oi.quantity) as productCount FROM Order o " +
            "INNER JOIN o.orderItems oi " +
            "INNER JOIN oi.product p " +
            "WHERE o.status = :status " +
            "AND oi.product = p.id " +
            "AND oi.order = o.id "+
            "GROUP BY p.name " +
            "ORDER BY productCount DESC")
    List<ProductSoldProjection> countProductsByOrderStatus(@Param("status") OrderStatus orderStatus);
}
