package us.mudflap.repository;

import us.mudflap.domain.TruckStop;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TruckStop entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TruckStopRepository extends JpaRepository<TruckStop, Long>, JpaSpecificationExecutor<TruckStop> {

}
