package uk.org.brooklyn.miniops.warehouse.dal.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import uk.org.brooklyn.miniops.common.dal.convertor.DeleteFlagConvertor;
import uk.org.brooklyn.miniops.common.dal.model.AuditableEntity;

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
@Table(name = "artifact")
public class Artifact extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            updatable = false
    )
    private String version;

    @Column(
            nullable = false,
            updatable = false
    )
    private Integer major;

    @Column(
            nullable = false,
            updatable = false
    )
    private Integer minor;

    @Column(
            nullable = false,
            updatable = false
    )
    private Integer patch;

    @Column(
            nullable = false,
            updatable = false,
            length = 64
    )
    private String prerelease;

    @Column(
            nullable = false,
            updatable = false,
            length = 64
    )
    private String buildMetadata;

    @ManyToOne
    @JoinColumn(
            name = "app_id",
            nullable = false
    )
    @ToString.Exclude
    private Application application;
}
