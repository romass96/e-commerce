package ua.ugolek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.ugolek.model.Archiveable;
import ua.ugolek.model.ArchivingDetails;
import ua.ugolek.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

@Service
@Transactional
public class ArchiveService
{
    private final AuditorAware<User> auditorProvider;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ArchiveService(AuditorAware<User> auditorProvider)
    {
        this.auditorProvider = auditorProvider;
    }

    public void moveToArchive(Archiveable archiveable) {
        ArchivingDetails archivingDetails = archiveable.getArchivingDetails();
        archivingDetails.setArchived(true);
        archivingDetails.setArchivedDate(LocalDateTime.now());
        auditorProvider.getCurrentAuditor().ifPresent(archivingDetails::setArchivedBy);

        entityManager.merge(archiveable);
    }

    public void restoreFromArchive(Archiveable archiveable) {
        ArchivingDetails archivingDetails = archiveable.getArchivingDetails();
        archivingDetails.setArchived(false);
        archivingDetails.setArchivedDate(null);
        archivingDetails.setArchivedBy(null);

        entityManager.merge(archiveable);
    }
}
