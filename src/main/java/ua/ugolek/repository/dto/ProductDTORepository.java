package ua.ugolek.repository.dto;

import org.springframework.stereotype.Repository;
import ua.ugolek.dto.ProductDTO;
import ua.ugolek.dto.mappers.ProductDTOMapper;
import ua.ugolek.model.Product;
import ua.ugolek.payload.filters.SearchFilter;
import ua.ugolek.repository.dto.extractors.DTOExtractor;
import ua.ugolek.repository.dto.extractors.ProductDTOExtractor;

@Repository
public class ProductDTORepository extends FilterSupportDTORepository<Product, SearchFilter, ProductDTO> {

    private ProductDTOMapper dtoMapper = new ProductDTOMapper();

    @Override
    protected DTOExtractor<Product, SearchFilter, ProductDTO> createDtoExtractor(SearchFilter filter) {
        return new ProductDTOExtractor(filter, entityManager, dtoMapper);
    }
}
