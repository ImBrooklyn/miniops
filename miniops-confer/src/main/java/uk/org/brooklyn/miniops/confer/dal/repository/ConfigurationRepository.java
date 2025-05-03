package uk.org.brooklyn.miniops.confer.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.org.brooklyn.miniops.confer.dal.model.Configuration;

import java.util.Optional;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

    Optional<Configuration> findByAppNameAndNamespaceAndDeployment(String appName, String namespace, String deployment);

    boolean existsByAppNameAndNamespaceAndDeployment(String appName, String namespace, String deployment);

}
