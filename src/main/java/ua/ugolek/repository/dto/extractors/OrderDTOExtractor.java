package ua.ugolek.repository.dto.extractors;

import ua.ugolek.dto.OrderDTO;
import ua.ugolek.model.Order;
import ua.ugolek.model.OrderItem;
import ua.ugolek.model.Product;
import ua.ugolek.payload.filters.OrderFilter;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class OrderDTOExtractor extends DTOExtractor<Order, OrderFilter, OrderDTO>
{
    // Names of entity's fields. Should be the same as names of Java class fields
    private static final String TOTAL_ORDER_PRICE_FIELD = "totalOrderPrice";
    private static final String COMMENT_FIELD = "comment";
    private static final String STATUS_FIELD = "status";
    private static final String PAID_FIELD = "paid";
    private static final String CREATED_DATE_FIELD = "createdDate";
    private static final String ID_FIELD = "id";
    private static final String PRODUCT_NAME_FIELD = "name";
    private static final String ORDER_ITEMS_FIELD = "orderItems";
    private static final String PRODUCT_FIELD = "product";

    public OrderDTOExtractor(OrderFilter filter, EntityManager entityManager,
        Function<Order, OrderDTO> dtoMapper)
    {
        super(filter, entityManager, dtoMapper);
    }

    @Override
    protected <P> void populateQuery(CriteriaQuery<P> query, From<?, Order> root)
    {
        List<Predicate> wherePredicates = new ArrayList<>();
        List<Predicate> stringForSearchPredicates = new ArrayList<>();

        Optional<String> stringForSearchOptional = filter.getStringForSearchOptional();
        Optional<Long> categoryIdOptional = filter.getCategoryIdOptional();
        if (stringForSearchOptional.isPresent() || categoryIdOptional.isPresent()) {
            Join<Order, OrderItem> orderItems = root.join(ORDER_ITEMS_FIELD);
            Join<OrderItem, Product> products = orderItems.join(PRODUCT_FIELD);

            categoryIdOptional.ifPresent(categoryId ->
                wherePredicates.add(criteriaBuilder.equal(products.get(ID_FIELD), categoryId)));

            stringForSearchOptional.ifPresent(stringForSearch ->
                stringForSearchPredicates.add(
                    createLikePredicate(products, PRODUCT_NAME_FIELD, stringForSearch)));
        }

        stringForSearchOptional.ifPresent(stringForSearch ->
            stringForSearchPredicates.add(createLikePredicate(root, COMMENT_FIELD, stringForSearch)));

        addOrderPredicates(wherePredicates, root);

        if (!stringForSearchPredicates.isEmpty()) {
            Predicate stringForSearchPredicate = criteriaBuilder.or(stringForSearchPredicates.toArray(new Predicate[0]));
            wherePredicates.add(stringForSearchPredicate);
        }

        Predicate[] wherePredicatesArray = wherePredicates.toArray(new Predicate[0]);
        query.where(wherePredicatesArray);
    }

    private void addOrderPredicates(List<Predicate> wherePredicates, From<?, Order> root) {
        filter.getToPriceOptional().ifPresent(toPrice ->
            wherePredicates.add(criteriaBuilder.lessThanOrEqualTo(
                root.get(TOTAL_ORDER_PRICE_FIELD), toPrice)));

        filter.getFromPriceOptional().ifPresent(fromPrice ->
            wherePredicates.add(criteriaBuilder.greaterThanOrEqualTo(
                root.get(TOTAL_ORDER_PRICE_FIELD), fromPrice)));

        filter.getFromDateOptional().ifPresent(fromDate ->
            wherePredicates.add(criteriaBuilder.greaterThanOrEqualTo(
                root.get(CREATED_DATE_FIELD), fromDate)));

        filter.getToDateOptional().ifPresent(toDate ->
            wherePredicates.add(criteriaBuilder.lessThanOrEqualTo(
                root.get(CREATED_DATE_FIELD), toDate)));

        filter.getPaidOptional().ifPresent(paid ->
            wherePredicates.add(criteriaBuilder.equal(root.get(PAID_FIELD), paid)));

        filter.getStatusOptional().ifPresent(status ->
            wherePredicates.add(criteriaBuilder.equal(root.get(STATUS_FIELD), status)));
    }
}
