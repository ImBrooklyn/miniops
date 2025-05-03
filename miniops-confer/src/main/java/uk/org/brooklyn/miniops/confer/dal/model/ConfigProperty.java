package uk.org.brooklyn.miniops.confer.dal.model;

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
 * @since 02/05/2025
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@SoftDelete(
        strategy = SoftDeleteType.DELETED,
        converter = DeleteFlagConvertor.class
)
@Table(name = "config_property")
public class ConfigProperty extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "config_id",
            nullable = false
    )
    @ToString.Exclude
    private Configuration configuration;

    @Column(
            nullable = false,
            updatable = false
    )
    private String propKey;

    @Column(
            nullable = false,
            length = 1024
    )
    private String propValue;

    @Column(
            nullable = false
    )
    private Boolean isSecret;
}
