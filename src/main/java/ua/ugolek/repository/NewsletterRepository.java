package ua.ugolek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.ugolek.model.Newsletter;

@Repository
public interface NewsletterRepository extends JpaRepository<Newsletter, Long>
{
}
