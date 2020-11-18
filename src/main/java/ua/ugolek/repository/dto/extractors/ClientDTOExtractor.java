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
import static ua.ugolek.dto.ClientDTO.EMAIL_FIELD;

public class ClientDTOExtractor extends DTOExtractor<Client, SearchFilter, ClientDTO>
{
    private static final String[] fieldNamesForSearch = new String[] {
        "firstName", "lastName", "email", "phoneNumber"
    };
    private static final String[] fieldNamesForSelect = new String[] {
        PHONE_NUMBER_FIELD, FIRST_NAME_FIELD, LAST_NAME_FIELD, EMAIL_FIELD
    };
    private static final String CLIENT_FIELD = "client";

    private Function<Tuple, ClientDTO> tupleToClientDTOMapper = new TupleToClientDTOMapper();

    public ClientDTOExtractor(SearchFilter filter, EntityManager entityManager)
    {
        super(filter, entityManager, null);
    }

    @Override
    protected <P> void populateQuery(CriteriaQuery<P> query, From<?, Client> root)
    {
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
        CriteriaQuery<Tuple> query = criteriaBuilder.createTupleQuery();
        Root<Order> orderRoot = query.from(Order.class);
        Join<Order, Client> clientJoin = orderRoot.join(CLIENT_FIELD);

        populateQuery(query, clientJoin);

        applySorting(query, clientJoin);
        applyGroupByOperation(query, clientJoin);
        applyMultiselectOperation(query, clientJoin, orderRoot);

        return query;
    }

    private void applyMultiselectOperation(CriteriaQuery<Tuple> query, Join<Order, Client> clientJoin, Root<Order> orderRoot) {
        List<Selection<?>> selections = Arrays.stream(fieldNamesForSelect)
            .map(fieldName -> clientJoin.get(fieldName).alias(fieldName))
            .collect(Collectors.toList());
        Selection<Long> ordersCountSelection = createOrdersCountSelection(orderRoot);
        selections.add(ordersCountSelection);
        query.multiselect(selections);
    }

    private void applyGroupByOperation(CriteriaQuery<Tuple> query, Join<Order, Client> clientJoin) {
        List<Expression<?>> groupByExpressions = Arrays.stream(fieldNamesForSelect)
            .map(clientJoin::get)
            .collect(Collectors.toList());
        query.groupBy(groupByExpressions);
    }

    private Selection<Long> createOrdersCountSelection(Root<Order> orderRoot) {
        return criteriaBuilder.count(orderRoot).alias(ORDERS_COUNT_ALIAS);
    }

    private static class TupleToClientDTOMapper implements Function<Tuple, ClientDTO>
    {
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
