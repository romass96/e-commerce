package ua.ugolek.dto.mappers;

import org.springframework.stereotype.Component;
import ua.ugolek.dto.ProductDTO;
import ua.ugolek.model.Product;

import java.util.function.Function;

@Component
public class ProductDTOMapper implements Function<Product, ProductDTO> {
    @Override
    public ProductDTO apply(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice().doubleValue())
                .categoryName(product.getCategory().getName())
                .quantity(product.getQuantity())
                .build();
    }
}
