package ua.ugolek.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Data
@NoArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "purchase_price", scale = 2)
    private BigDecimal purchasePrice;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    @NotNull
    private Integer quantity;

    @Transient
    public BigDecimal getTotalPrice() {
        return purchasePrice.multiply(new BigDecimal(quantity));
    }

    @PrePersist
    public void calculatePurchasePrice() {
        this.purchasePrice = product.getPrice();
    }
}
