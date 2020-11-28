package ua.ugolek.controller.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.ugolek.dto.NewsletterDTO;
import ua.ugolek.model.Newsletter;
import ua.ugolek.model.Order;
import ua.ugolek.payload.ListResponse;
import ua.ugolek.payload.filters.SearchFilter;
import ua.ugolek.service.NewsletterService;
import ua.ugolek.service.dto.NewsletterDTOService;

import javax.validation.ConstraintViolationException;

@RestController
@RequestMapping("/api/newsletters")
@Slf4j
public class NewsletterController
{
    @Autowired
    private NewsletterDTOService newsletterDTOService;

    @Autowired
    private NewsletterService newsletterService;

    @PostMapping("/filter")
    public ListResponse<NewsletterDTO> getNewslettersByFilter(@RequestBody SearchFilter filter) {
        return newsletterDTOService.queryByFilter(filter);
    }

    @PostMapping("")
    public HttpStatus createNewsletter(@RequestBody Newsletter newsletter)
    {
        newsletterService.create(newsletter);
        return HttpStatus.CREATED;
    }
}
