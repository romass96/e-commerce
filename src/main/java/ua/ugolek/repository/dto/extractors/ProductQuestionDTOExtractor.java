package ua.ugolek.repository.dto.extractors;

import ua.ugolek.dto.ProductQuestionDTO;
import ua.ugolek.dto.mappers.ProductQuestionDTOMapper;
import ua.ugolek.model.Category;
import ua.ugolek.model.Product;
import ua.ugolek.model.ProductQuestion;
import ua.ugolek.payload.filters.ProductQuestionFilter;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ProductQuestionDTOExtractor extends DTOExtractor<ProductQuestion, ProductQuestionFilter, ProductQuestionDTO>
{
    private static final String PRODUCT_FIELD = "product";
    private static final String PRODUCT_NAME_FIELD = "name";
    private static final String TEXT_FIELD = "text";
    private static final String CREATED_DATE_FIELD = "createdDate";
    private static final String CATEGORY_FIELD = "category";

    public ProductQuestionDTOExtractor(ProductQuestionFilter filter, EntityManager entityManager,
        ProductQuestionDTOMapper dtoMapper)
    {
        super(filter, entityManager, dtoMapper);
    }

    @Override
    protected <P> void populateQuery(CriteriaQuery<P> query, From<?, ProductQuestion> root)
    {
        PredicateCreator predicateCreator = new PredicateCreator(criteriaBuilder);

        Optional<String> stringForSearchOptional = filter.getStringForSearchOptional();
        Optional<Long> categoryIdOptional = filter.getCategoryIdOptional();
        if (categoryIdOptional.isPresent() || stringForSearchOptional.isPresent()) {
            Join<ProductQuestion, Product> product = root.join(PRODUCT_FIELD);
            Join<Product, Category> category = product.join(CATEGORY_FIELD);

            categoryIdOptional.ifPresent(categoryId ->
                predicateCreator.addEqualToPredicate(category.get(ID_FIELD), categoryId));

            stringForSearchOptional.ifPresent(stringForSearch -> {
                List<Expression<String>> expressions = Arrays.asList(
                    product.get(PRODUCT_NAME_FIELD),
                    root.get(TEXT_FIELD));
                predicateCreator.addStringSearch(stringForSearch, expressions);
            });
        }

        filter.getFromDateOptional().ifPresent(fromDate ->
            predicateCreator.addGreaterThanOrEqualToPredicate(root.get(CREATED_DATE_FIELD), fromDate));

        filter.getToDateOptional().ifPresent(toDate ->
            predicateCreator.addLessThanOrEqualToPredicate(root.get(CREATED_DATE_FIELD), toDate));

        query.where(predicateCreator.getPredicatesAsArray());
    }
}
