package ua.ugolek.payload.filters;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class ProductFilter extends SearchFilter
{
    private boolean archived;
    private Double fromPrice;
    private Double toPrice;
    private Long categoryId;

    public Optional<Double> getFromPriceOptional() {
        return Optional.ofNullable(fromPrice);
    }

    public Optional<Double> getToPriceOptional() {
        return Optional.ofNullable(toPrice);
    }

    public Optional<Long> getCategoryIdOptional() {
        return Optional.ofNullable(categoryId);
    }
}
