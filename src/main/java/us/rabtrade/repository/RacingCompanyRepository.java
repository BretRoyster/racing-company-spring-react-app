package us.rabtrade.repository;

import us.rabtrade.domain.RacingCompany;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the RacingCompany entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RacingCompanyRepository extends JpaRepository<RacingCompany, Long>, JpaSpecificationExecutor<RacingCompany> {

    @Query("select racingCompany from RacingCompany racingCompany where racingCompany.owner.login = ?#{principal.username}")
    List<RacingCompany> findByOwnerIsCurrentUser();

}
