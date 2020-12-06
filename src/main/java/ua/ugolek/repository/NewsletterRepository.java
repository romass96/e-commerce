package ua.ugolek.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.ugolek.model.Newsletter;
import ua.ugolek.model.NewsletterStatus;

@Repository
public interface NewsletterRepository extends BaseEntityRepository<Newsletter>
{
    @Modifying
    @Query("UPDATE Newsletter n SET n.status = :status WHERE n.id = :id")
    @Transactional
    void updateStatusById(@Param("id") Long id, @Param("status") NewsletterStatus status);
}
