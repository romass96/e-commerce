package ua.ugolek.dto.mappers;

import ua.ugolek.dto.NewsletterDTO;
import ua.ugolek.model.Newsletter;

import java.util.function.Function;

public class NewsletterDTOMapper implements Function<Newsletter, NewsletterDTO>
{
    @Override public NewsletterDTO apply(Newsletter newsletter)
    {
        return NewsletterDTO.builder()
            .id(newsletter.getId())
            .subject(newsletter.getSubject())
            .createdDate(newsletter.getCreatedDate())
            .dispatchDate(newsletter.getDispatchDate())
            .createdBy(newsletter.getCreatedBy().getFullName())
            .status(newsletter.getStatus())
            .build();
    }
}
