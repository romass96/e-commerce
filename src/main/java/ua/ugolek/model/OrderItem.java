package ua.ugolek.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@NoArgsConstructor
@Entity
@Table(name = "order_items")
@Data
public class OrderItem extends BaseEntity {

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
    @Min(1)
    private Integer quantity;

    @Transient
    public BigDecimal getTotalPrice() {
        if (purchasePrice == null) {
            calculatePurchasePrice();
        }
        return purchasePrice.multiply(new BigDecimal(quantity));
    }

    @PrePersist
    public void calculatePurchasePrice() {
        this.purchasePrice = product.getPrice();
    }
}
