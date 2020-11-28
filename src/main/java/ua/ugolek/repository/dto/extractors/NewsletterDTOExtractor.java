package ua.ugolek.repository.dto.extractors;

import ua.ugolek.dto.NewsletterDTO;
import ua.ugolek.model.Newsletter;
import ua.ugolek.payload.filters.SearchFilter;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.util.function.Function;
import java.util.stream.Stream;

public class NewsletterDTOExtractor extends DTOExtractor<Newsletter, SearchFilter, NewsletterDTO>
{
    private static final String[] fieldNamesForSearch = new String[]{"htmlContent"};

    public NewsletterDTOExtractor(SearchFilter filter, EntityManager entityManager,
        Function<Newsletter, NewsletterDTO> dtoMapper)
    {
        super(filter, entityManager, dtoMapper);
    }

    @Override
    protected <P> void populateQuery(CriteriaQuery<P> query, From<?, Newsletter> root)
    {
        filter.getStringForSearchOptional().ifPresent(stringForSearch -> {
            Predicate[] predicates = Stream.of(fieldNamesForSearch)
                .map(field -> createLikePredicate(root, field, stringForSearch))
                .toArray(Predicate[]::new);
            query.where(criteriaBuilder.or(predicates));
        });
    }
}
