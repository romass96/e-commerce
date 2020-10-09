package ua.ugolek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.ugolek.model.PropertyDefinition;

@Repository
public interface PropertyDefinitionRepository extends JpaRepository<PropertyDefinition, Long> {

}
