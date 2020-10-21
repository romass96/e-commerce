package ua.ugolek.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ua.ugolek.model.Order;
import ua.ugolek.model.OrderItem;
import ua.ugolek.model.Product;
import ua.ugolek.payload.OrderFilter;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
public class ExtendedOrderRepository {

    // Names of entity's fields. Should be the same as names of Java class fields
    private static final String TOTAL_ORDER_PRICE_FIELD = "totalOrderPrice";
    private static final String COMMENT_FIELD = "comment";
    private static final String STATUS_FIELD = "status";
    private static final String CREATED_DATE_FIELD = "createdDate";
    private static final String ID_FIELD = "id";
    private static final String PRODUCT_NAME_FIELD = "name";
    private static final String ORDER_ITEMS_FIELD = "orderItems";
    private static final String PRODUCT_FIELD = "product";

    @PersistenceContext
    private EntityManager entityManager;

    private CriteriaBuilder criteriaBuilder;

    @PostConstruct
    public void setupCriteriaBuilder() {
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<Order> filter(OrderFilter orderFilter, Pageable pageable) {
        CriteriaQuery<Long> countQuery = getCountQuery(orderFilter);
        long count = entityManager.createQuery(countQuery).getSingleResult();

        CriteriaQuery<Order> query = getSelectQuery(orderFilter);
        TypedQuery<Order> typedQuery = entityManager.createQuery(query);
        setPaginationParameters(typedQuery, orderFilter.getPageNumber(), orderFilter.getPerPage());

        return new PageImpl<>(typedQuery.getResultList(), pageable, count);
    }

    private CriteriaQuery<Order> getSelectQuery(OrderFilter orderFilter) {
        CriteriaQuery<Order> query = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        populateQuery(orderFilter, query, root);
        orderFilter.getSortByOptional().ifPresent(sortBy -> {
            Function<Path<Order>, javax.persistence.criteria.Order> sortFunction = orderFilter.isSortDesc() ?
                    criteriaBuilder::desc : criteriaBuilder::asc;
            query.orderBy(sortFunction.apply(root.get(sortBy)));
        });
        return query.select(root);
    }

    private CriteriaQuery<Long> getCountQuery(OrderFilter orderFilter) {
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Order> root = query.from(Order.class);
        populateQuery(orderFilter, query, root);
        return query.select(criteriaBuilder.count(root));
    }

    private <T> void populateQuery(OrderFilter orderFilter, CriteriaQuery<T> query, Root<Order> root) {
        List<Predicate> wherePredicates = new ArrayList<>();
        List<Predicate> stringForSearchPredicates = new ArrayList<>();

        Optional<String> stringForSearchOptional = orderFilter.getStringForSearchOptional();

        orderFilter.getCategoryIdOptional().ifPresent(categoryId -> {
            Join<Order, OrderItem> orderItems = root.join(ORDER_ITEMS_FIELD);
            Join<OrderItem, Product> products = orderItems.join(PRODUCT_FIELD);
            wherePredicates.add(criteriaBuilder.equal(products.get(ID_FIELD), categoryId));

            stringForSearchOptional.ifPresent(stringForSearch ->
                stringForSearchPredicates.add(
                    criteriaBuilder.like(products.get(PRODUCT_NAME_FIELD), like(stringForSearch))));
        });

        orderFilter.getStatusOptional().ifPresent(status ->
                wherePredicates.add(criteriaBuilder.equal(root.get(STATUS_FIELD), status)));

        stringForSearchOptional.ifPresent(stringForSearch ->
            stringForSearchPredicates.add(criteriaBuilder.like(root.get(COMMENT_FIELD), like(stringForSearch))));

        orderFilter.getToPriceOptional().ifPresent(toPrice ->
                wherePredicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get(TOTAL_ORDER_PRICE_FIELD), toPrice)));

        orderFilter.getFromPriceOptional().ifPresent(fromPrice ->
                wherePredicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get(TOTAL_ORDER_PRICE_FIELD), fromPrice)));

        orderFilter.getFromDateOptional().ifPresent(fromDate ->
                wherePredicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get(CREATED_DATE_FIELD), fromDate)));

        orderFilter.getToDateOptional().ifPresent(toDate ->
                wherePredicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get(CREATED_DATE_FIELD), toDate)));

        Predicate stringForSearchPredicate = criteriaBuilder.or(stringForSearchPredicates.toArray(new Predicate[0]));
        wherePredicates.add(stringForSearchPredicate);
        Predicate[] wherePredicatesArray = wherePredicates.toArray(new Predicate[0]);

        query.where(wherePredicatesArray);
    }

    private void setPaginationParameters(TypedQuery<Order> query, int pageNumber, int perPage) {
        query.setFirstResult((pageNumber - 1) * perPage);
        query.setMaxResults(perPage);
    }

    private String like(String input) {
        return "%" + input + "%";
    }
}
