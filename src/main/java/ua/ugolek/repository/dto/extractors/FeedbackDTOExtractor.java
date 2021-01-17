package ua.ugolek.repository.dto.extractors;

import ua.ugolek.dto.FeedbackDTO;
import ua.ugolek.model.Category;
import ua.ugolek.model.Feedback;
import ua.ugolek.model.Product;
import ua.ugolek.payload.filters.FeedbackFilter;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class FeedbackDTOExtractor extends DTOExtractor<Feedback, FeedbackFilter, FeedbackDTO> {
    private static final String ADVANTAGES_FIELD = "advantages";
    private static final String DISADVANTAGES_FIELD = "disadvantages";
    private static final String TEXT_FIELD = "text";
    private static final String CREATED_DATE_FIELD = "createdDate";
    private static final String NAME_FIELD = "name";
    private static final String ID_FIELD = "id";
    private static final String PRODUCT_FIELD = "product";
    private static final String CATEGORY_FIELD = "category";
    private static final String RATING_FIELD = "rating";

    public FeedbackDTOExtractor(FeedbackFilter filter, EntityManager entityManager,
                                Function<Feedback, FeedbackDTO> dtoMapper) {
        super(filter, entityManager, dtoMapper);
    }

    @Override
    protected <P> void populateQuery(CriteriaQuery<P> query, From<?, Feedback> root) {
        List<Predicate> wherePredicates = new ArrayList<>();

        Optional<String> stringForSearchOptional = filter.getStringForSearchOptional();
        Optional<Long> categoryIdOptional = filter.getCategoryIdOptional();
        if (categoryIdOptional.isPresent() || stringForSearchOptional.isPresent()) {
            Join<Feedback, Product> product = root.join(PRODUCT_FIELD);
            Join<Product, Category> category = product.join(CATEGORY_FIELD);

            categoryIdOptional.ifPresent(categoryId ->
                wherePredicates.add(createEqualPredicate(category.get(ID_FIELD), categoryId)));

            stringForSearchOptional.ifPresent(stringForSearch -> {
                List<Expression<String>> stringForSearchExpressions = new ArrayList<>();

                stringForSearchExpressions.add(product.get(NAME_FIELD));
                stringForSearchExpressions.add(root.get(TEXT_FIELD));
                stringForSearchExpressions.add(root.get(ADVANTAGES_FIELD));
                stringForSearchExpressions.add(root.get(DISADVANTAGES_FIELD));

                Predicate stringForSearchPredicate = getStringSearchPredicate(stringForSearch, stringForSearchExpressions);
                wherePredicates.add(stringForSearchPredicate);
            });
        }

        filter.getFromDateOptional().ifPresent(fromDate ->
                wherePredicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get(CREATED_DATE_FIELD), fromDate)));

        filter.getToDateOptional().ifPresent(toDate ->
                wherePredicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get(CREATED_DATE_FIELD), toDate)));

        wherePredicates.add(criteriaBuilder.between(root.get(RATING_FIELD),
                filter.getFromRating(), filter.getToRating()));

        Predicate[] wherePredicatesArray = wherePredicates.toArray(new Predicate[0]);

        query.where(wherePredicatesArray);
    }
}
