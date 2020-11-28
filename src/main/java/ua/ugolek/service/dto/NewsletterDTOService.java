package ua.ugolek.service.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.ugolek.dto.NewsletterDTO;
import ua.ugolek.model.Newsletter;
import ua.ugolek.payload.filters.SearchFilter;
import ua.ugolek.repository.dto.NewsletterDTORepository;

@Service
public class NewsletterDTOService extends FilterSupportDTOService<Newsletter, SearchFilter, NewsletterDTO>
{
    @Autowired
    public NewsletterDTOService(NewsletterDTORepository filterSupportRepository)
    {
        super(filterSupportRepository);
    }
}
