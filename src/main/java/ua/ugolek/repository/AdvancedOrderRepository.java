package ua.ugolek.repository;

import org.springframework.stereotype.Repository;
import ua.ugolek.model.Order;
import ua.ugolek.model.OrderItem;
import ua.ugolek.model.Product;
import ua.ugolek.payload.filters.OrderFilter;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public class AdvancedOrderRepository extends FilterSupportRepository<Order, OrderFilter> {

    // Names of entity's fields. Should be the same as names of Java class fields
    private static final String TOTAL_ORDER_PRICE_FIELD = "totalOrderPrice";
    private static final String COMMENT_FIELD = "comment";
    private static final String STATUS_FIELD = "status";
    private static final String CREATED_DATE_FIELD = "createdDate";
    private static final String ID_FIELD = "id";
    private static final String PRODUCT_NAME_FIELD = "name";
    private static final String ORDER_ITEMS_FIELD = "orderItems";
    private static final String PRODUCT_FIELD = "product";

    @Override
    protected  <T> void populateQuery(OrderFilter orderFilter, CriteriaQuery<T> query, Root<Order> root) {
        List<Predicate> wherePredicates = new ArrayList<>();
        List<Predicate> stringForSearchPredicates = new ArrayList<>();

        Optional<String> stringForSearchOptional = orderFilter.getStringForSearchOptional();
        Optional<Long> categoryIdOptional = orderFilter.getCategoryIdOptional();
        if (stringForSearchOptional.isPresent() || categoryIdOptional.isPresent()) {
            Join<Order, OrderItem> orderItems = root.join(ORDER_ITEMS_FIELD);
            Join<OrderItem, Product> products = orderItems.join(PRODUCT_FIELD);

            categoryIdOptional.ifPresent(categoryId ->
                    wherePredicates.add(criteriaBuilder.equal(products.get(ID_FIELD), categoryId)));

            stringForSearchOptional.ifPresent(stringForSearch ->
                    stringForSearchPredicates.add(
                            criteriaBuilder.like(products.get(PRODUCT_NAME_FIELD), like(stringForSearch))));
        }

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

        if (!stringForSearchPredicates.isEmpty()) {
            Predicate stringForSearchPredicate = criteriaBuilder.or(stringForSearchPredicates.toArray(new Predicate[0]));
            wherePredicates.add(stringForSearchPredicate);
        }

        Predicate[] wherePredicatesArray = wherePredicates.toArray(new Predicate[0]);
        query.where(wherePredicatesArray);
    }
}
