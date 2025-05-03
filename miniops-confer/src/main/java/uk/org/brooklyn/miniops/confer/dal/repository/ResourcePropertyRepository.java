package uk.org.brooklyn.miniops.confer.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.org.brooklyn.miniops.confer.dal.model.ResourceProperty;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
@Repository
public interface ResourcePropertyRepository extends JpaRepository<ResourceProperty, Long> {
}
