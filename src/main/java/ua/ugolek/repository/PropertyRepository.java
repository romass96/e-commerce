package ua.ugolek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.ugolek.model.Property;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

}
