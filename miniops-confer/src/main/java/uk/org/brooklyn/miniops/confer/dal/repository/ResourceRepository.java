package uk.org.brooklyn.miniops.confer.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.org.brooklyn.miniops.confer.dal.model.Resource;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    boolean existsByNameAndNamespace(String name, String namespace);
}
