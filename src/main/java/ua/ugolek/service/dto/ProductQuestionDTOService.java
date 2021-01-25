package ua.ugolek.service.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.ugolek.dto.ProductQuestionDTO;
import ua.ugolek.model.ProductQuestion;
import ua.ugolek.payload.filters.ProductQuestionFilter;
import ua.ugolek.repository.dto.ProductQuestionDTORepository;

@Service
public class ProductQuestionDTOService extends FilterSupportDTOService<ProductQuestion, ProductQuestionFilter, ProductQuestionDTO>
{
    @Autowired
    protected ProductQuestionDTOService(ProductQuestionDTORepository filterSupportRepository)
    {
        super(filterSupportRepository);
    }
}
