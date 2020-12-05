package ua.ugolek.repository;

import org.springframework.stereotype.Repository;
import ua.ugolek.model.Newsletter;

@Repository
public interface NewsletterRepository extends BaseEntityRepository<Newsletter>
{
}
