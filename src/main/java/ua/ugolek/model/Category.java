package ua.ugolek.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Setter
@Getter
public class Category extends BaseEntity {

    @NotBlank
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category parentCategory;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PropertyDefinition> definitions = new ArrayList<>();

    public void addDefinition(PropertyDefinition definition) {
       this.definitions.add(definition);
    }

}
