package uk.org.brooklyn.miniops.confer.dal.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import uk.org.brooklyn.miniops.common.dal.convertor.DeleteFlagConvertor;
import uk.org.brooklyn.miniops.common.dal.model.AuditableEntity;
import uk.org.brooklyn.miniops.confer.common.enums.ResourceType;

import java.util.List;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@SoftDelete(
        strategy = SoftDeleteType.DELETED,
        converter = DeleteFlagConvertor.class
)
@Table(name = "resource")
public class Resource extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            updatable = false,
            length = 64
    )
    private String name;

    @Column(
            nullable = false,
            updatable = false,
            length = 32
    )
    private String namespace;

    @Column(
            nullable = false,
            updatable = false,
            length = 32
    )
    @Enumerated(value = EnumType.STRING)
    private ResourceType type;

    @OneToMany(
            mappedBy = "resource",
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.REMOVE
            }
    )

    @ToString.Exclude
    private List<ResourceProperty> resourceProperties;

    @ManyToMany
    @JoinTable(
            name = "config_resource",
            joinColumns = @JoinColumn(name = "resource_id"),
            inverseJoinColumns = @JoinColumn(name = "config_id")
    )
    @ToString.Exclude
    private List<Configuration> configurations;
}
