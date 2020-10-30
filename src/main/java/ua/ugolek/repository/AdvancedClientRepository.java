package ua.ugolek.repository;

import org.springframework.stereotype.Repository;
import ua.ugolek.model.Client;
import ua.ugolek.model.Order;
import ua.ugolek.model.OrderItem;
import ua.ugolek.model.Product;
import ua.ugolek.payload.filters.SearchFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class AdvancedClientRepository extends FilterSupportRepository<Client, SearchFilter> {
    private static final String FIRST_NAME_FIELD = "firstName";
    private static final String LAST_NAME_FIELD = "lastName";
    private static final String EMAIL_FIELD = "email";
    private static final String PHONE_FIELD = "phoneNumber";

    public AdvancedClientRepository() {
        super(Client.class);
    }

    public Map<Date, Long> countClientsByRegistrationDate() {
        Stream<Tuple> stream = (Stream<Tuple>) entityManager.createNativeQuery(
                "SELECT DATE(c.registrationDate) as registrationDate, COUNT(c) as clientsCount " +
                        "FROM clients c GROUP BY regDate", Tuple.class);

        return stream.collect(Collectors.toMap(tuple -> (Date)tuple.get("registrationDate"),
                        tuple -> (Long)tuple.get("clientsCount")));
    }

    @Override
    protected <P> void populateQuery(SearchFilter filter, CriteriaQuery<P> query, Root<Client> root) {
        filter.getStringForSearchOptional().ifPresent(stringForSearch -> {
            Predicate[] predicates = Stream.of(FIRST_NAME_FIELD, LAST_NAME_FIELD, EMAIL_FIELD, PHONE_FIELD)
                    .map(field -> criteriaBuilder.like(root.get(field), like(stringForSearch)))
                    .toArray(Predicate[]::new);
            query.where(criteriaBuilder.or(predicates));
        });
    }
}
