package ua.ugolek.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import ua.ugolek.config.JmsConfiguration;
import ua.ugolek.model.Newsletter;
import ua.ugolek.model.NewsletterStatus;
import ua.ugolek.repository.NewsletterRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ua.ugolek.Constants.ZONE_OFFSET;

@Service
@Slf4j
public class NewsletterService extends CRUDService<Newsletter>
{
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Autowired
    private NewsletterRepository newsletterRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private JmsTemplate jmsTemplate;

    private final Map<Long, Long> requestMap = new ConcurrentHashMap<>();

    @Autowired
    public NewsletterService(NewsletterRepository baseEntityRepository)
    {
        super(baseEntityRepository);
    }

    @Override
    //TODO Implement failed scenario
    public Newsletter create(Newsletter newsletter) {
        final Newsletter newsletterWithId = newsletterRepository.save(newsletter);
        Runnable task = () -> {
            log.debug("Scheduled task for newsletter with id={} is running.", newsletterWithId.getId());
            newsletterRepository.updateStatusById(newsletterWithId.getId(), NewsletterStatus.IN_PROGRESS);
            sendNewsletterRequestsToClients(newsletterWithId);
        };
        threadPoolTaskScheduler.schedule(task, newsletter.getDispatchDate().toInstant(ZONE_OFFSET));
        return newsletterWithId;
    }

    private void sendNewsletterRequestsToClients(Newsletter newsletter) {
        long clientCount = clientService.countAll();
        clientService.processAllEntities(client -> {
            NewsletterRequest request = NewsletterRequest.builder()
                .clientEmail(client.getEmail())
                .newsletterId(newsletter.getId())
                .subject(newsletter.getSubject())
                .htmlContent(newsletter.getHtmlContent())
                .totalClientCount(clientCount)
                .build();
            sendNewsletterRequest(request);
        });
    }

    @JmsListener(destination = JmsConfiguration.NEWSLETTER_QUEUE,
        containerFactory = JmsConfiguration.CONNECTION_FACTORY)
    public void receiveNewsletterRequest(NewsletterRequest request) {
        log.debug("Newsletter request {} is received", request);

        String subject = request.getSubject();
        String to = request.getClientEmail();
        String content = request.getHtmlContent();
        mailService.sendMessage(to, subject, content);

        increaseCounter(request);
        checkIfNewsletterIsCompleted(request);

        log.debug("Email is sent for newsletter request {}", request);
    }

    private void sendNewsletterRequest(NewsletterRequest request) {
        jmsTemplate.convertAndSend(JmsConfiguration.NEWSLETTER_QUEUE, request);
        log.debug("Newsletter request {} is sent", request);
    }

    private void increaseCounter(NewsletterRequest request) {
        Long newsletterId = request.getNewsletterId();
        requestMap.putIfAbsent(newsletterId, 0L);
        requestMap.computeIfPresent(newsletterId, (key, value) -> ++value);
    }

    private void checkIfNewsletterIsCompleted(NewsletterRequest request) {
        Long newsletterId = request.getNewsletterId();
        if ( requestMap.get(newsletterId) == request.totalClientCount) {
            log.debug("Newsletter with id {} is completed", newsletterId);
            requestMap.remove(newsletterId);
            newsletterRepository.updateStatusById(newsletterId, NewsletterStatus.COMPLETED);
        }
    }

    @NoArgsConstructor
    @Data
    @Builder
    @AllArgsConstructor
    private static class NewsletterRequest {
        private String clientEmail;
        private String subject;
        private String htmlContent;
        private Long newsletterId;
        private long totalClientCount;
    }

}
