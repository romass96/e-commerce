package ua.ugolek.repository;

import ua.ugolek.payload.filters.SearchFilter;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.stream.Stream;

public abstract class StringSearchRepository<T> extends FilterSupportRepository<T, SearchFilter> {
    private final String[] fieldNamesForSearch;

    protected StringSearchRepository(String[] fieldNamesForSearch) {
        this.fieldNamesForSearch = fieldNamesForSearch;
    }

    @Override
    protected <P> void populateQuery(SearchFilter filter, CriteriaQuery<P> query, Root<T> root) {
        filter.getStringForSearchOptional().ifPresent(stringForSearch -> {
            Predicate[] predicates = Stream.of(fieldNamesForSearch)
                    .map(field -> criteriaBuilder.like(root.get(field), like(stringForSearch)))
                    .toArray(Predicate[]::new);
            query.where(criteriaBuilder.or(predicates));
        });
    }

}
