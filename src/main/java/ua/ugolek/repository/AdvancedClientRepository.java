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
public class AdvancedClientRepository extends StringSearchRepository<Client> {
    public AdvancedClientRepository() {
        super(new String[] {"firstName", "lastName", "email", "phoneNumber"});
    }

}
