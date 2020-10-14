package ua.ugolek.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ua.ugolek.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
public class OrderFilter {
    private int pageNumber;
    private int perPage;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime fromDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime toDate;
    private Long categoryId;

    private OrderStatus status;

    private Double fromPrice;
    private Double toPrice;

    private String stringForSearch;

    private String sortBy;
    private boolean sortDesc;


    public Optional<String> getStringForSearchOptional() {
        return Optional.ofNullable(stringForSearch);
    }

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

    public Optional<String> getSortByOptional() {
        return Optional.ofNullable(sortBy);
    }
}
