package ua.ugolek.repository.dto.extractors;

import ua.ugolek.dto.FeedbackDTO;
import ua.ugolek.model.Category;
import ua.ugolek.model.Feedback;
import ua.ugolek.model.Product;
import ua.ugolek.payload.filters.FeedbackFilter;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class FeedbackDTOExtractor extends DTOExtractor<Feedback, FeedbackFilter, FeedbackDTO> {
    private static final String ADVANTAGES_FIELD = "advantages";
    private static final String DISADVANTAGES_FIELD = "disadvantages";
    private static final String TEXT_FIELD = "text";
    private static final String CREATED_DATE_FIELD = "createdDate";
    private static final String NAME_FIELD = "name";
    private static final String PRODUCT_FIELD = "product";
    private static final String CATEGORY_FIELD = "category";
    private static final String RATING_FIELD = "rating";

    public FeedbackDTOExtractor(FeedbackFilter filter, EntityManager entityManager,
                                Function<Feedback, FeedbackDTO> dtoMapper) {
        super(filter, entityManager, dtoMapper);
    }

    @Override
    protected <P> void populateQuery(CriteriaQuery<P> query, From<?, Feedback> root) {
        PredicateCreator predicateCreator = new PredicateCreator(criteriaBuilder);

        Optional<String> stringForSearchOptional = filter.getStringForSearchOptional();
        Optional<Long> categoryIdOptional = filter.getCategoryIdOptional();
        if (categoryIdOptional.isPresent() || stringForSearchOptional.isPresent()) {
            Join<Feedback, Product> product = root.join(PRODUCT_FIELD);
            Join<Product, Category> category = product.join(CATEGORY_FIELD);

            categoryIdOptional.ifPresent(categoryId ->
                predicateCreator.addEqualToPredicate(category.get(ID_FIELD), categoryId));

            stringForSearchOptional.ifPresent(stringForSearch -> {
                List<Expression<String>> expressions = Arrays.asList(
                    product.get(NAME_FIELD),
                    root.get(TEXT_FIELD),
                    root.get(ADVANTAGES_FIELD),
                    root.get(DISADVANTAGES_FIELD));
                predicateCreator.addStringSearch(stringForSearch, expressions);
            });
        }

        filter.getFromDateOptional().ifPresent(fromDate ->
            predicateCreator.addGreaterThanOrEqualToPredicate(root.get(CREATED_DATE_FIELD), fromDate));

        filter.getToDateOptional().ifPresent(toDate ->
            predicateCreator.addLessThanOrEqualToPredicate(root.get(CREATED_DATE_FIELD), toDate));

        predicateCreator.addBetweenPredicate(root.get(RATING_FIELD), filter.getFromRating(), filter.getToRating());

        query.where(predicateCreator.getPredicatesAsArray());
    }
}
