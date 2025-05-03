package uk.org.brooklyn.miniops.confer.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.org.brooklyn.miniops.confer.dal.model.ConfigProperty;
import uk.org.brooklyn.miniops.confer.dal.model.Configuration;

import java.util.Collection;
import java.util.Optional;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
@Repository
public interface ConfigPropertyRepository extends JpaRepository<ConfigProperty, Long> {
    boolean existsByConfigurationAndPropKeyIn(Configuration configuration, Collection<String> propKeys);

    Optional<ConfigProperty> findByConfigurationAndPropKey(Configuration configuration, String propKey);
}
