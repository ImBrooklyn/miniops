package uk.org.brooklyn.miniops.confer.dal.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import uk.org.brooklyn.miniops.common.dal.convertor.DeleteFlagConvertor;
import uk.org.brooklyn.miniops.common.dal.model.AuditableEntity;

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
@Table(name = "configuration")
public class Configuration extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            updatable = false,
            length = 64
    )
    private String appName;

    @Column(
            nullable = false,
            updatable = false,
            length = 32
    )
    private String namespace;

    @Column(
            nullable = false,
            updatable = false
    )
    private String deployment;

    @OneToMany(mappedBy = "configuration")
    @ToString.Exclude
    private List<ConfigProperty> configProperties;

    @ManyToMany
    @JoinTable(
            name = "config_resource",
            joinColumns = @JoinColumn(name = "config_id"),
            inverseJoinColumns = @JoinColumn(name = "resource_id")
    )
    @ToString.Exclude
    private List<Resource> resources;
}
