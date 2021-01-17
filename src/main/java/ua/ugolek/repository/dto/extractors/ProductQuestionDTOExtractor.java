package ua.ugolek.repository.dto.extractors;

import ua.ugolek.dto.ProductQuestionDTO;
import ua.ugolek.dto.mappers.ProductQuestionDTOMapper;
import ua.ugolek.model.Product;
import ua.ugolek.model.ProductQuestion;
import ua.ugolek.payload.filters.SearchFilter;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.Arrays;
import java.util.List;

public class ProductQuestionDTOExtractor extends DTOExtractor<ProductQuestion, SearchFilter, ProductQuestionDTO>
{
    private static final String PRODUCT_FIELD = "product";
    private static final String PRODUCT_NAME_FIELD = "name";
    private static final String TEXT_FIELD = "text";

    public ProductQuestionDTOExtractor(SearchFilter filter, EntityManager entityManager,
        ProductQuestionDTOMapper dtoMapper)
    {
        super(filter, entityManager, dtoMapper);
    }

    @Override
    protected <P> void populateQuery(CriteriaQuery<P> query, From<?, ProductQuestion> root)
    {
        filter.getStringForSearchOptional().ifPresent(stringForSearch -> {
            Join<ProductQuestion, Product> product = root.join(PRODUCT_FIELD);

            List<Expression<String>> expressions = Arrays.asList(
                product.get(PRODUCT_NAME_FIELD),
                root.get(TEXT_FIELD));
            Predicate predicate = getStringSearchPredicate(stringForSearch, expressions);
            query.where(predicate);
        });
    }
}
