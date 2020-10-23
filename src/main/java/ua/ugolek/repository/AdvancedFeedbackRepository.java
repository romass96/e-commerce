package ua.ugolek.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ua.ugolek.model.Category;
import ua.ugolek.model.Feedback;
import ua.ugolek.model.Product;
import ua.ugolek.payload.FeedbackFilter;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static ua.ugolek.util.RepositoryUtils.like;
import static ua.ugolek.util.RepositoryUtils.setPaginationParameters;

@Repository
public class AdvancedFeedbackRepository {

    private static final String ADVANTAGES_FIELD = "advantages";
    private static final String DISADVANTAGES_FIELD = "disadvantages";
    private static final String TEXT_FIELD = "text";
    private static final String CREATED_DATE_FIELD = "createdDate";
    private static final String NAME_FIELD = "name";
    private static final String ID_FIELD = "id";
    private static final String PRODUCT_FIELD = "product";
    private static final String CATEGORY_FIELD = "category";
    private static final String RATING_FIELD = "rating";

    @PersistenceContext
    private EntityManager entityManager;

    private CriteriaBuilder criteriaBuilder;

    @PostConstruct
    public void setupCriteriaBuilder() {
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<Feedback> filter(FeedbackFilter filter, Pageable pageable) {
        CriteriaQuery<Long> countQuery = getCountQuery(filter);
        long count = entityManager.createQuery(countQuery).getSingleResult();

        CriteriaQuery<Feedback> query = getSelectQuery(filter);
        TypedQuery<Feedback> typedQuery = entityManager.createQuery(query);
        setPaginationParameters(typedQuery, filter.getPageNumber(), filter.getPerPage());

        return new PageImpl<>(typedQuery.getResultList(), pageable, count);
    }

    private CriteriaQuery<Feedback> getSelectQuery(FeedbackFilter feedbackFilter) {
        CriteriaQuery<Feedback> query = criteriaBuilder.createQuery(Feedback.class);
        Root<Feedback> root = query.from(Feedback.class);
        populateQuery(feedbackFilter, query, root);
        feedbackFilter.getSortByOptional().ifPresent(sortBy -> {
            Function<Path<Feedback>, Order> sortFunction = feedbackFilter.isSortDesc() ?
                    criteriaBuilder::desc : criteriaBuilder::asc;
            query.orderBy(sortFunction.apply(root.get(sortBy)));
        });
        return query.select(root);
    }

    private CriteriaQuery<Long> getCountQuery(FeedbackFilter feedbackFilter) {
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Feedback> root = query.from(Feedback.class);
        populateQuery(feedbackFilter, query, root);
        return query.select(criteriaBuilder.count(root));
    }

    private <T> void populateQuery(FeedbackFilter feedbackFilter, CriteriaQuery<T> query, Root<Feedback> root) {
        List<Predicate> wherePredicates = new ArrayList<>();
        List<Predicate> stringForSearchPredicates = new ArrayList<>();

        Optional<String> stringForSearchOptional = feedbackFilter.getStringForSearchOptional();
        Optional<Long> categoryIdOptional = feedbackFilter.getCategoryIdOptional();
        if (categoryIdOptional.isPresent() || stringForSearchOptional.isPresent()) {
            Join<Feedback, Product> product = root.join(PRODUCT_FIELD);
            Join<Product, Category> category = product.join(CATEGORY_FIELD);

            categoryIdOptional.ifPresent(categoryId ->
                    wherePredicates.add(criteriaBuilder.equal(category.get(ID_FIELD), categoryId)));
            stringForSearchOptional.ifPresent(stringForSearch ->
                    stringForSearchPredicates.add(criteriaBuilder.like(product.get(NAME_FIELD), like(stringForSearch))));
        }

        stringForSearchOptional.ifPresent(stringForSearch ->
                Stream.of(TEXT_FIELD, ADVANTAGES_FIELD, DISADVANTAGES_FIELD)
                        .map(field -> criteriaBuilder.like(root.get(field), like(stringForSearch)))
                        .forEach(stringForSearchPredicates::add));

        feedbackFilter.getFromDateOptional().ifPresent(fromDate ->
                wherePredicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get(CREATED_DATE_FIELD), fromDate)));

        feedbackFilter.getToDateOptional().ifPresent(toDate ->
                wherePredicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get(CREATED_DATE_FIELD), toDate)));

        wherePredicates.add(criteriaBuilder.between(root.get(RATING_FIELD),
                feedbackFilter.getFromRating(), feedbackFilter.getToRating()));

        if (!stringForSearchPredicates.isEmpty()) {
            Predicate stringForSearchPredicate = criteriaBuilder.or(stringForSearchPredicates.toArray(new Predicate[0]));
            wherePredicates.add(stringForSearchPredicate);
        }

        Predicate[] wherePredicatesArray = wherePredicates.toArray(new Predicate[0]);

        query.where(wherePredicatesArray);
    }

}
