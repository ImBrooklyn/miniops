package uk.org.brooklyn.miniops.warehouse.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.org.brooklyn.miniops.warehouse.dal.model.AppExposure;

/**
 * @author ImBrooklyn
 * @since 26/04/2025
 */
@Repository
public interface AppExposureRepository extends JpaRepository<AppExposure, Long> {
}
