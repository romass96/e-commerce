package ua.ugolek.payload.filters;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ua.ugolek.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
public class OrderFilter extends SearchFilter {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime fromDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime toDate;

    private Long categoryId;

    private OrderStatus status;
    private Boolean paid;

    private Double fromPrice;
    private Double toPrice;

    public Optional<Long> getCategoryIdOptional() {
        return Optional.ofNullable(categoryId);
    }

    public Optional<OrderStatus> getStatusOptional() {
        return Optional.ofNullable(status);
    }

    public Optional<Double> getFromPriceOptional() {
        return Optional.ofNullable(fromPrice);
    }

    public Optional<Double> getToPriceOptional() {
        return Optional.ofNullable(toPrice);
    }

    public Optional<LocalDateTime> getFromDateOptional() {
        return Optional.ofNullable(fromDate);
    }

    public Optional<LocalDateTime> getToDateOptional() {
        return Optional.ofNullable(toDate);
    }

    public Optional<Boolean> getPaidOptional() {
        return Optional.ofNullable(paid);
    }

}
