package uk.org.brooklyn.miniops.warehouse.dal.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import uk.org.brooklyn.miniops.warehouse.enums.ExposureType;
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
@Table(name = "app_exposure")
public class AppExposure extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "app_id",
            nullable = false
    )
    @ToString.Exclude
    private Application application;

    @Column(
            nullable = false,
            updatable = false,
            length = 32
    )
    @Enumerated(value = EnumType.STRING)
    private ExposureType type;

    @Column(
            nullable = false,
            updatable = false,
            length = 64
    )
    private String path;

    @Column(
            nullable = false,
            updatable = false
    )
    private Integer port;

    private String params;

}
