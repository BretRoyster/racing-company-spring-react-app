package us.rabtrade.repository.search;

import us.rabtrade.domain.TruckStop;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link TruckStop} entity.
 */
public interface TruckStopSearchRepository extends ElasticsearchRepository<TruckStop, Long> {
}
