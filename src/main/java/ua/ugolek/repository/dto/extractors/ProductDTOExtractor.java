package ua.ugolek.repository.dto.extractors;

import ua.ugolek.dto.ProductDTO;
import ua.ugolek.model.Product;
import ua.ugolek.payload.filters.SearchFilter;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.util.function.Function;
import java.util.stream.Stream;

public class ProductDTOExtractor extends DTOExtractor<Product, SearchFilter, ProductDTO> {
    private static final String[] fieldNamesForSearch = new String[]{"name", "description"};

    public ProductDTOExtractor(SearchFilter filter, EntityManager entityManager,
                               Function<Product, ProductDTO> dtoMapper) {
        super(filter, entityManager, dtoMapper);
    }

    @Override
    protected <P> void populateQuery(CriteriaQuery<P> query, From<?, Product> root) {
        filter.getStringForSearchOptional().ifPresent(stringForSearch -> {
            Predicate[] predicates = Stream.of(fieldNamesForSearch)
                    .map(field -> createLikePredicate(root, field, stringForSearch))
                    .toArray(Predicate[]::new);
            query.where(criteriaBuilder.or(predicates));
        });
    }
}
