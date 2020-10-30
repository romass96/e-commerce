package ua.ugolek.repository;

import org.springframework.stereotype.Repository;
import ua.ugolek.model.Product;
import ua.ugolek.payload.filters.SearchFilter;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.stream.Stream;

@Repository
public class AdvancedProductRepository extends FilterSupportRepository<Product, SearchFilter> {

    private static final String NAME_FIELD = "name";
    private static final String DESCRIPTION_FIELD = "description";

    protected AdvancedProductRepository() {
        super(Product.class);
    }

    @Override
    protected <P> void populateQuery(SearchFilter filter, CriteriaQuery<P> query, Root<Product> root) {
        filter.getStringForSearchOptional().ifPresent(stringForSearch -> {
            Predicate[] predicates = Stream.of(NAME_FIELD, DESCRIPTION_FIELD)
                    .map(field -> criteriaBuilder.like(root.get(field), like(stringForSearch)))
                    .toArray(Predicate[]::new);
            query.where(criteriaBuilder.or(predicates));
        });
    }
}
