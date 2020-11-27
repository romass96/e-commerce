package ua.ugolek.dto.mappers;

import ua.ugolek.dto.ProductDTO;
import ua.ugolek.model.Product;

import java.util.function.Function;

public class ProductDTOMapper implements Function<Product, ProductDTO> {
    @Override
    public ProductDTO apply(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice().doubleValue())
                .categoryName(product.getCategory().getName())
                .quantity(product.getQuantity())
                .archived(product.getArchivingDetails().isArchived())
                .build();
    }
}
