package ua.ugolek.dto.mappers;

import ua.ugolek.dto.ProductQuestionDTO;
import ua.ugolek.model.ProductQuestion;

import java.util.function.Function;

public class ProductQuestionDTOMapper implements Function<ProductQuestion, ProductQuestionDTO>
{
    @Override public ProductQuestionDTO apply(ProductQuestion productQuestion)
    {
        return ProductQuestionDTO.builder()
            .id(productQuestion.getId())
            .clientFullName(productQuestion.getClient().getFullName())
            .productName(productQuestion.getProduct().getName())
            .clientId(productQuestion.getClient().getId())
            .createdDate(productQuestion.getCreatedDate())
            .build();
    }
}
