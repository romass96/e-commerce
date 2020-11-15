package ua.ugolek.repository.dto;

import org.springframework.stereotype.Repository;
import ua.ugolek.dto.ClientDTO;
import ua.ugolek.model.Client;
import ua.ugolek.model.Order;
import ua.ugolek.payload.filters.SearchFilter;

import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ua.ugolek.dto.ClientDTO.*;

@Repository
public class ClientDTORepository extends FilterSupportDTORepository<Client, SearchFilter, ClientDTO> {
    private static final String[] fieldNamesForSearch = new String[] {
            "firstName", "lastName", "email", "phoneNumber"
    };
    private static final String CLIENT_FIELD = "client";

    private Function<Tuple, ClientDTO> tupleToClientDTOMapper = new TupleToClientDTOMapper();

    @Override
    protected <P> void populateQuery(SearchFilter filter, CriteriaQuery<P> query, From<?, Client> root) {
        filter.getStringForSearchOptional().ifPresent(stringForSearch -> {
            Predicate[] predicates = Stream.of(fieldNamesForSearch)
                    .map(field -> createLikePredicate(root, field, stringForSearch))
                    .toArray(Predicate[]::new);
            query.where(criteriaBuilder.or(predicates));
        });
    }

    @Override
    protected List<ClientDTO> queryDTOList(SearchFilter filter) {
        CriteriaQuery<Tuple> query = createSelectTupleQuery(filter);
        TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
        setPaginationParameters(typedQuery, filter.getPageNumber(), filter.getPerPage());

        return typedQuery.getResultList().stream()
                .map(tupleToClientDTOMapper)
                .collect(Collectors.toList());
    }

    private CriteriaQuery<Tuple> createSelectTupleQuery(SearchFilter filter) {
        CriteriaQuery<Tuple> query = criteriaBuilder.createTupleQuery();
        Root<Order> orderRoot = query.from(Order.class);
        Join<Order, Client> clientJoin = orderRoot.join(CLIENT_FIELD);

        populateQuery(filter, query, clientJoin);
        applySorting(filter, query, clientJoin);

        Selection<Long> ordersCountSelection = createOrdersCountSelection(orderRoot);
        List<Selection<?>> selections = new ArrayList<>();
        List<Expression<?>> groupByExpressions = new ArrayList<>();
        Stream.of(PHONE_NUMBER_FIELD, FIRST_NAME_FIELD, LAST_NAME_FIELD, EMAIL_FIELD).forEach(fieldName -> {
            selections.add(clientJoin.get(fieldName).alias(fieldName));
            groupByExpressions.add(clientJoin.get(fieldName));
        });
        selections.add(ordersCountSelection);
        return query.multiselect(selections).groupBy(groupByExpressions);
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
