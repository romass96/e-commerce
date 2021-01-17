package ua.ugolek.repository.dto.extractors;

import ua.ugolek.dto.NewsletterDTO;
import ua.ugolek.model.Newsletter;
import ua.ugolek.payload.filters.SearchFilter;

import javax.persistence.EntityManager;
import java.util.function.Function;

public class NewsletterDTOExtractor extends DTOExtractorWithStringSearch<Newsletter, SearchFilter, NewsletterDTO>
{
    private static final String[] fieldNamesForSearch = new String[]{"htmlContent", "subject"};

    public NewsletterDTOExtractor(SearchFilter filter, EntityManager entityManager,
        Function<Newsletter, NewsletterDTO> dtoMapper)
    {
        super(filter, entityManager, dtoMapper);
    }

    @Override
    protected String[] getFieldNamesForSearch()
    {
        return fieldNamesForSearch;
    }
}
