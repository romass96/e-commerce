package ua.ugolek.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDTO implements DTO {
    private Long id;
    private String name;
    private String categoryName;
    private double price;
    private int quantity;
    private boolean archived;
}
