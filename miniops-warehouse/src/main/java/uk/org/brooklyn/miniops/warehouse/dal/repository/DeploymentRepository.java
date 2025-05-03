package uk.org.brooklyn.miniops.warehouse.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.org.brooklyn.miniops.warehouse.dal.model.Deployment;

import java.util.Optional;

/**
 * @author ImBrooklyn
 * @since 26/04/2025
 */
@Repository
public interface DeploymentRepository extends JpaRepository<Deployment, Long> {

    boolean existsByName(String name);

    Optional<Deployment> findByName(String name);
}
