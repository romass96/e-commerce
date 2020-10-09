package ua.ugolek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.ugolek.model.Client;
import ua.ugolek.projection.ClientsRegistrationProjection;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query(value = "SELECT DATE(c.registration_date) as registrationDate, COUNT(c) as clientsCount FROM clients c " +
            "WHERE c.registration_date >= :startDate " +
            "GROUP BY registrationDate " +
            "ORDER BY registrationDate",
            nativeQuery = true)
    List<ClientsRegistrationProjection> countClientsByRegistrationDate(@Param("startDate") LocalDateTime startDate);
}
