package us.rabtrade.repository;

import us.rabtrade.domain.TruckStop;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the TruckStop entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TruckStopRepository extends JpaRepository<TruckStop, Long>, JpaSpecificationExecutor<TruckStop> {

    @Query("select truckStop from TruckStop truckStop where truckStop.owner.login = ?#{principal.username}")
    List<TruckStop> findByOwnerIsCurrentUser();

}
