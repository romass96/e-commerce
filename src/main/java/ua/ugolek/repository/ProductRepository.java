package ua.ugolek.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.ugolek.model.OrderStatus;
import ua.ugolek.model.Product;
import ua.ugolek.projection.ClientProductsByCategories;
import ua.ugolek.projection.ProductSoldProjection;
import ua.ugolek.projection.SoldProductsByDate;

import java.util.List;

@Repository
public interface ProductRepository extends BaseEntityRepository<Product> {

    @Query("SELECT p.name as productName, SUM(oi.quantity) as productCount FROM Order o " +
            "INNER JOIN o.orderItems oi " +
            "INNER JOIN oi.product p " +
            "WHERE o.status = :status " +
//            "AND oi.product.id = p.id " +
//            "AND oi.order.id = o.id "+
            "GROUP BY productName " +
            "ORDER BY productCount DESC")
    List<ProductSoldProjection> countProductsByOrderStatus(@Param("status") OrderStatus orderStatus);

    @Query("SELECT COUNT(o) FROM Order o JOIN o.client c")
    Long orders(@Param("status") OrderStatus orderStatus);

    @Query(value = "SELECT cat.name as categoryName, SUM(oi.quantity) as productCount FROM orders o " +
        "INNER JOIN order_items oi ON oi.order_id = o.id " +
        "INNER JOIN products p ON oi.product_id = p.id " +
        "INNER JOIN categories cat ON cat.id = p.category_id " +
        "INNER JOIN clients c ON o.client_id = c.id " +
        "WHERE c.id = :clientId " +
        "AND o.status = 'COMPLETED'" +
        "GROUP BY categoryName",
        nativeQuery = true)
    List<ClientProductsByCategories> countProductsForCategories(@Param("clientId") Long clientId);

    @Query(value = "SELECT DATE(o.created_date) as orderDate, SUM(oi.quantity) as productCount FROM orders o " +
        "INNER JOIN order_items oi ON oi.order_id = o.id " +
        "INNER JOIN products p ON oi.product_id = p.id " +
        "WHERE p.id = :productId " +
        "GROUP BY orderDate",
        nativeQuery = true)
    List<SoldProductsByDate> countProductsInOrdersByDate(@Param("productId") Long productId);

    @Modifying
    @Query("UPDATE Product p SET p.quantity = :quantity WHERE p.id = :id")
    void updateProductQuantity(@Param("id") Long userId, @Param("quantity") Long quantity);
}
