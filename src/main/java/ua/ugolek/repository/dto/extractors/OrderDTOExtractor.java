package ua.ugolek.repository.dto.extractors;

import ua.ugolek.dto.OrderDTO;
import ua.ugolek.model.*;
import ua.ugolek.model.Order;
import ua.ugolek.payload.filters.OrderFilter;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class OrderDTOExtractor extends DTOExtractor<Order, OrderFilter, OrderDTO> {
    // Names of entity's fields. Should be the same as names of Java class fields
    private static final String TOTAL_ORDER_PRICE_FIELD = "totalOrderPrice";
    private static final String COMMENT_FIELD = "comment";
    private static final String STATUS_FIELD = "status";
    private static final String PAID_FIELD = "paid";
    private static final String CREATED_DATE_FIELD = "createdDate";
    private static final String PRODUCT_NAME_FIELD = "name";
    private static final String ORDER_ITEMS_FIELD = "orderItems";
    private static final String PRODUCT_FIELD = "product";
    private static final String CLIENT_FIELD = "client";
    private static final String CATEGORY_FIELD = "category";

    public OrderDTOExtractor(OrderFilter filter, EntityManager entityManager,
                             Function<Order, OrderDTO> dtoMapper) {
        super(filter, entityManager, dtoMapper);
    }

    @Override
    protected <P> void populateQuery(CriteriaQuery<P> query, From<?, Order> root) {
        PredicateCreator predicateCreator = new PredicateCreator(criteriaBuilder);

        Optional<String> stringForSearchOptional = filter.getStringForSearchOptional();
        Optional<Long> categoryIdOptional = filter.getCategoryIdOptional();
        if (stringForSearchOptional.isPresent() || categoryIdOptional.isPresent()) {
            Join<Order, OrderItem> orderItems = root.join(ORDER_ITEMS_FIELD);
            Join<OrderItem, Product> products = orderItems.join(PRODUCT_FIELD);
            Join<Product, Category> category = products.join(CATEGORY_FIELD);

            categoryIdOptional.ifPresent(categoryId ->
                predicateCreator.addEqualToPredicate(category.get(ID_FIELD), categoryId));

            stringForSearchOptional.ifPresent(stringForSearch -> {
                List<Expression<String>> expressions = Arrays.asList(
                    products.get(PRODUCT_NAME_FIELD),
                    root.get(COMMENT_FIELD));
                predicateCreator.addStringSearch(stringForSearch, expressions);
            });
        }

        addOrderPredicates(predicateCreator, root);
        query.where(predicateCreator.getPredicatesAsArray());
    }

    private void addOrderPredicates(PredicateCreator predicateCreator, From<?, Order> root) {
        filter.getToPriceOptional().ifPresent(toPrice ->
            predicateCreator.addLessThanOrEqualToPredicate(root.get(TOTAL_ORDER_PRICE_FIELD), toPrice));

        filter.getFromPriceOptional().ifPresent(fromPrice ->
            predicateCreator.addGreaterThanOrEqualToPredicate(root.get(TOTAL_ORDER_PRICE_FIELD), fromPrice));

        filter.getFromDateOptional().ifPresent(fromDate ->
            predicateCreator.addGreaterThanOrEqualToPredicate(root.get(CREATED_DATE_FIELD), fromDate));

        filter.getToDateOptional().ifPresent(toDate ->
            predicateCreator.addLessThanOrEqualToPredicate(root.get(CREATED_DATE_FIELD), toDate));

        filter.getPaidOptional().ifPresent(paid ->
            predicateCreator.addEqualToPredicate(root.get(PAID_FIELD), paid));

        filter.getStatusOptional().ifPresent(status ->
            predicateCreator.addEqualToPredicate(root.get(STATUS_FIELD), status));

        filter.getClientIdOptional().ifPresent(clientId -> {
            Join<Order, Client> client = root.join(CLIENT_FIELD);
            predicateCreator.addEqualToPredicate(client.get(ID_FIELD), clientId);
        });
    }

}
