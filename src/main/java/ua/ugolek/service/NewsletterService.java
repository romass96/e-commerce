package ua.ugolek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import ua.ugolek.model.Newsletter;
import ua.ugolek.repository.NewsletterRepository;

import static ua.ugolek.Constants.ZONE_OFFSET;

@Service
public class NewsletterService extends CRUDService<Newsletter>
{
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Autowired
    private NewsletterRepository newsletterRepository;

    @Autowired
    private MailService mailService;

    //TODO Implement message sending
//    @Override
//    public Newsletter create(Newsletter newsletter) {
//        final Newsletter savedNewsletter = newsletterRepository.save(newsletter);
//        Runnable task = () -> {
//            savedNewsletter.setDispatched(true);
//            newsletterRepository.save(savedNewsletter);
//        };
//        threadPoolTaskScheduler.schedule(task, newsletter.getDispatchDate().toInstant(ZONE_OFFSET));
//        return savedNewsletter;
//    }

    @Override
    protected JpaRepository<Newsletter, Long> getRepository()
    {
        return newsletterRepository;
    }
}
