package ua.ugolek.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "products")
@NoArgsConstructor
@Data
public class Product extends Auditable<User> implements Archiveable {

    @NotBlank
    private String name;

    @Column(name = "price", scale = 2)
    private BigDecimal price;

    @NotBlank
    private String description;

    private String pictureUrl;

    @Min(0)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Property> properties = new ArrayList<>();

    @Embedded
    private ArchivingDetails archivingDetails = new ArchivingDetails();

    public List<PropertyDefinition> getPropertyDefinitions() {
        return properties.stream().map(Property::getDefinition).collect(Collectors.toList());
    }
}
