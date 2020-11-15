package ua.ugolek.repository.dto;

import org.springframework.stereotype.Repository;
import ua.ugolek.dto.FeedbackDTO;
import ua.ugolek.dto.mappers.FeedbackDTOMapper;
import ua.ugolek.model.Category;
import ua.ugolek.model.Feedback;
import ua.ugolek.model.Product;
import ua.ugolek.payload.filters.FeedbackFilter;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class FeedbackDTORepository extends FilterSupportDTORepository<Feedback, FeedbackFilter, FeedbackDTO>{
    private static final String ADVANTAGES_FIELD = "advantages";
    private static final String DISADVANTAGES_FIELD = "disadvantages";
    private static final String TEXT_FIELD = "text";
    private static final String CREATED_DATE_FIELD = "createdDate";
    private static final String NAME_FIELD = "name";
    private static final String ID_FIELD = "id";
    private static final String PRODUCT_FIELD = "product";
    private static final String CATEGORY_FIELD = "category";
    private static final String RATING_FIELD = "rating";

    @PostConstruct
    public void initDTOMapper() {
        this.dtoMapper = new FeedbackDTOMapper();
    }

    @Override
    protected <P> void populateQuery(FeedbackFilter filter, CriteriaQuery<P> query, From<?, Feedback> root) {
        List<Predicate> wherePredicates = new ArrayList<>();
        List<Predicate> stringForSearchPredicates = new ArrayList<>();

        Optional<String> stringForSearchOptional = filter.getStringForSearchOptional();
        Optional<Long> categoryIdOptional = filter.getCategoryIdOptional();
        if (categoryIdOptional.isPresent() || stringForSearchOptional.isPresent()) {
            Join<Feedback, Product> product = root.join(PRODUCT_FIELD);
            Join<Product, Category> category = product.join(CATEGORY_FIELD);

            categoryIdOptional.ifPresent(categoryId ->
                    wherePredicates.add(criteriaBuilder.equal(category.get(ID_FIELD), categoryId)));
            stringForSearchOptional.ifPresent(stringForSearch ->
                    stringForSearchPredicates.add(createLikePredicate(product, NAME_FIELD, stringForSearch)));
        }

        stringForSearchOptional.ifPresent(stringForSearch ->
                Stream.of(TEXT_FIELD, ADVANTAGES_FIELD, DISADVANTAGES_FIELD)
                        .map(field -> createLikePredicate(root, field, stringForSearch))
                        .forEach(stringForSearchPredicates::add));

        filter.getFromDateOptional().ifPresent(fromDate ->
                wherePredicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get(CREATED_DATE_FIELD), fromDate)));

        filter.getToDateOptional().ifPresent(toDate ->
                wherePredicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get(CREATED_DATE_FIELD), toDate)));

        wherePredicates.add(criteriaBuilder.between(root.get(RATING_FIELD),
                filter.getFromRating(), filter.getToRating()));

        if (!stringForSearchPredicates.isEmpty()) {
            Predicate stringForSearchPredicate = criteriaBuilder.or(stringForSearchPredicates.toArray(new Predicate[0]));
            wherePredicates.add(stringForSearchPredicate);
        }

        Predicate[] wherePredicatesArray = wherePredicates.toArray(new Predicate[0]);

        query.where(wherePredicatesArray);
    }
}
