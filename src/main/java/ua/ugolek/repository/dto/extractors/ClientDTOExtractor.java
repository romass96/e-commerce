package ua.ugolek.repository.dto.extractors;

import ua.ugolek.dto.ClientDTO;
import ua.ugolek.model.Client;
import ua.ugolek.model.Order;
import ua.ugolek.payload.filters.SearchFilter;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ua.ugolek.dto.ClientDTO.*;

public class ClientDTOExtractor extends DTOExtractor<Client, SearchFilter, ClientDTO> {
    private static final String[] fieldNamesForSearch = new String[]{
            "firstName", "lastName", "email", "phoneNumber"
    };
    private static final String[] fieldNamesForSelect = new String[]{
            PHONE_NUMBER_FIELD, FIRST_NAME_FIELD, LAST_NAME_FIELD, EMAIL_FIELD
    };
    private static final String CLIENT_FIELD = "client";

    private final Function<Tuple, ClientDTO> tupleToClientDTOMapper = new TupleToClientDTOMapper();
    private Join<Order, Client> clientJoin;
    private Root<Order> orderRoot;
    private CriteriaQuery<Tuple> tupleQuery;

    public ClientDTOExtractor(SearchFilter filter, EntityManager entityManager) {
        super(filter, entityManager, null);
    }

    @Override
    protected <P> void populateQuery(CriteriaQuery<P> query, From<?, Client> root) {
        filter.getStringForSearchOptional().ifPresent(stringForSearch -> {
            Predicate[] predicates = Stream.of(fieldNamesForSearch)
                    .map(field -> createLikePredicate(root, field, stringForSearch))
                    .toArray(Predicate[]::new);
            query.where(criteriaBuilder.or(predicates));
        });
    }

    @Override
    public List<ClientDTO> queryDTOList() {
        CriteriaQuery<Tuple> query = createSelectTupleQuery();
        TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
        setPaginationParameters(typedQuery);

        return typedQuery.getResultList().stream()
                .map(tupleToClientDTOMapper)
                .collect(Collectors.toList());
    }

    private CriteriaQuery<Tuple> createSelectTupleQuery() {
        this.tupleQuery = criteriaBuilder.createTupleQuery();

        initRoots();
        populateQuery(tupleQuery, clientJoin);

        applySorting(tupleQuery, clientJoin);
        applyGroupByOperation();
        applyMultiselectOperation();

        return tupleQuery;
    }

    private void initRoots() {
        this.orderRoot = tupleQuery.from(Order.class);
        this.clientJoin = orderRoot.join(CLIENT_FIELD);
    }

    private void applyMultiselectOperation() {
        List<Selection<?>> selections = Arrays.stream(fieldNamesForSelect)
                .map(fieldName -> clientJoin.get(fieldName).alias(fieldName))
                .collect(Collectors.toList());
        Selection<Long> ordersCountSelection = createOrdersCountSelection(orderRoot);
        selections.add(ordersCountSelection);
        tupleQuery.multiselect(selections);
    }

    private void applyGroupByOperation() {
        List<Expression<?>> groupByExpressions = Arrays.stream(fieldNamesForSelect)
                .map(clientJoin::get)
                .collect(Collectors.toList());
        tupleQuery.groupBy(groupByExpressions);
    }

    private Selection<Long> createOrdersCountSelection(Root<Order> orderRoot) {
        return criteriaBuilder.count(orderRoot).alias(ORDERS_COUNT_ALIAS);
    }

    private static class TupleToClientDTOMapper implements Function<Tuple, ClientDTO> {
        @Override
        public ClientDTO apply(Tuple tuple) {
            return ClientDTO.builder()
                    .firstName(tuple.get(FIRST_NAME_FIELD, String.class))
                    .lastName(tuple.get(LAST_NAME_FIELD, String.class))
                    .phoneNumber(tuple.get(PHONE_NUMBER_FIELD, String.class))
                    .email(tuple.get(EMAIL_FIELD, String.class))
                    .ordersCount(tuple.get(ORDERS_COUNT_ALIAS, Long.class))
                    .build();
        }
    }
}
