package ua.ugolek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.ugolek.model.Property;
import ua.ugolek.model.PropertyDefinition;
import ua.ugolek.repository.PropertyDefinitionRepository;
import ua.ugolek.repository.PropertyRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PropertyService {

    @Autowired
    private PropertyDefinitionRepository definitionRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    public PropertyDefinition createDefinition(PropertyDefinition definition) {
        return definitionRepository.save(definition);
    }

    public Property createProperty(Property property) {
        return propertyRepository.save(property);
    }

    public List<Property> createProperties(List<Property> properties) {
        return new ArrayList<>(propertyRepository.saveAll(properties));
    }


}
