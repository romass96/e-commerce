package ua.ugolek.repository.dto.extractors;

import ua.ugolek.dto.DTO;
import ua.ugolek.payload.filters.SearchFilter;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class DTOExtractorWithStringSearch<T, F extends SearchFilter, U extends DTO> extends DTOExtractor<T, F , U>
{
    public DTOExtractorWithStringSearch(F filter, EntityManager entityManager,
        Function<T, U> dtoMapper)
    {
        super(filter, entityManager, dtoMapper);
    }

    @Override
    protected <P> void populateQuery(CriteriaQuery<P> query, From<?, T> root)
    {
        filter.getStringForSearchOptional().ifPresent(stringForSearch -> {
            List<Expression<String>> expressions = Stream.of(getFieldNamesForSearch())
                .map(fieldName -> getFieldExpression(root, fieldName))
                .collect(Collectors.toList());
            Predicate predicate = getStringSearchPredicate(stringForSearch, expressions);
            query.where(predicate);
        });
    }

    protected abstract String[] getFieldNamesForSearch();
}
