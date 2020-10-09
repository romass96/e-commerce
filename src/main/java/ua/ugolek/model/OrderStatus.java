package ua.ugolek.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum OrderStatus {
    CANCELLED,
    PENDING,
    AWAITING_PAYMENT,
    AWAITING_SHIPMENT,
    SHIPPED,
    COMPLETED
}
