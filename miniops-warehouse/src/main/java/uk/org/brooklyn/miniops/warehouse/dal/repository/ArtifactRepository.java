package uk.org.brooklyn.miniops.warehouse.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.org.brooklyn.miniops.warehouse.dal.model.Application;
import uk.org.brooklyn.miniops.warehouse.dal.model.Artifact;

import java.util.Optional;

/**
 * @author ImBrooklyn
 * @since 26/04/2025
 */
@Repository
public interface ArtifactRepository extends JpaRepository<Artifact, Long> {

    Optional<Artifact> findByApplicationAndVersion(Application application, String version);

}
