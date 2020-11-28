package ua.ugolek.repository.dto;

import org.springframework.stereotype.Repository;
import ua.ugolek.dto.NewsletterDTO;
import ua.ugolek.dto.mappers.NewsletterDTOMapper;
import ua.ugolek.model.Newsletter;
import ua.ugolek.payload.filters.SearchFilter;
import ua.ugolek.repository.dto.extractors.DTOExtractor;
import ua.ugolek.repository.dto.extractors.NewsletterDTOExtractor;

@Repository
public class NewsletterDTORepository extends FilterSupportDTORepository<Newsletter, SearchFilter, NewsletterDTO>
{
    private NewsletterDTOMapper dtoMapper = new NewsletterDTOMapper();

    @Override
    protected DTOExtractor<Newsletter, SearchFilter, NewsletterDTO> createDtoExtractor(SearchFilter filter)
    {
        return new NewsletterDTOExtractor(filter, entityManager, dtoMapper);
    }
}
