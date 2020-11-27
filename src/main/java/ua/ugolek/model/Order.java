package ua.ugolek.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Order extends Auditable<User> {

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    @OneToMany(mappedBy = "order", orphanRemoval = true, cascade = CascadeType.ALL)
    @Size(min = 1, message = "Order should contain at least one order item")
    @Valid
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private String phoneNumber;

    private String comment;

    @Column(name = "total_order_price", scale = 2)
    private BigDecimal totalOrderPrice;

    private boolean paid;

//    @OneToOne
//    @JoinColumn(name = "delivery_details_id")
//    private DeliveryDetails deliveryDetails;

    @PrePersist
    public void calculateTotalOrderPrice() {
        this.totalOrderPrice = orderItems.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transient
    public int getNumberOfProducts() {
        return this.orderItems.size();
    }

    public void addItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
    }
}
