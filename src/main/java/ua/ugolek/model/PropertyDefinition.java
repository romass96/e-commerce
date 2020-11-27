package ua.ugolek.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "property_definitions")
@NoArgsConstructor
@Getter
@Setter
public class PropertyDefinition extends BaseEntity {

    private String name;
}
