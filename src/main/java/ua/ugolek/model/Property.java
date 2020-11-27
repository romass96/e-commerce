package ua.ugolek.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "properties")
@NoArgsConstructor
@Getter
@Setter
public class Property extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "definition_id")
    private PropertyDefinition definition;

    private String value;

}
