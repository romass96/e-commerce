package ua.ugolek.repository.dto;

import org.springframework.stereotype.Repository;
import ua.ugolek.dto.ProductQuestionDTO;
import ua.ugolek.dto.mappers.ProductQuestionDTOMapper;
import ua.ugolek.model.ProductQuestion;
import ua.ugolek.payload.filters.ProductQuestionFilter;
import ua.ugolek.repository.dto.extractors.DTOExtractor;
import ua.ugolek.repository.dto.extractors.ProductQuestionDTOExtractor;

@Repository
public class ProductQuestionDTORepository extends FilterSupportDTORepository<ProductQuestion, ProductQuestionFilter, ProductQuestionDTO>
{
    private ProductQuestionDTOMapper dtoMapper = new ProductQuestionDTOMapper();

    @Override
    protected DTOExtractor<ProductQuestion, ProductQuestionFilter, ProductQuestionDTO> createDtoExtractor(
        ProductQuestionFilter filter)
    {
        return new ProductQuestionDTOExtractor(filter, entityManager, dtoMapper);
    }
}
