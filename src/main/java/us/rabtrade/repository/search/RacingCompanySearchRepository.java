package us.rabtrade.repository.search;

import us.rabtrade.domain.RacingCompany;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link RacingCompany} entity.
 */
public interface RacingCompanySearchRepository extends ElasticsearchRepository<RacingCompany, Long> {
}
