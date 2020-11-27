package ua.ugolek.service.dto;

import org.springframework.stereotype.Service;
import ua.ugolek.dto.ProductDTO;
import ua.ugolek.model.Product;
import ua.ugolek.payload.filters.ProductFilter;
import ua.ugolek.repository.dto.ProductDTORepository;

@Service
public class ProductDTOService extends FilterSupportDTOService<Product, ProductFilter, ProductDTO> {

    public ProductDTOService(ProductDTORepository productDTORepository) {
        super(productDTORepository);
    }
}
