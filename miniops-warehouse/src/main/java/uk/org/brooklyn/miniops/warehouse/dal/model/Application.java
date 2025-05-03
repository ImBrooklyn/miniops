package uk.org.brooklyn.miniops.warehouse.dal.model;

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
 * @since 26/04/2025
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@SoftDelete(
        strategy = SoftDeleteType.DELETED,
        converter = DeleteFlagConvertor.class
)
@Table(name = "application")
public class Application extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            unique = true,
            nullable = false,
            updatable = false,
            length = 64
    )
    private String name;

    @OneToMany(mappedBy = "application")
    @ToString.Exclude
    private List<Artifact> artifacts;

    @OneToMany(
            mappedBy = "application",
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.REMOVE
            }
    )
    @ToString.Exclude
    private List<AppExposure> appExposures;

    @OneToMany(mappedBy = "application")
    @ToString.Exclude
    private List<Deployment> deployments;

}

