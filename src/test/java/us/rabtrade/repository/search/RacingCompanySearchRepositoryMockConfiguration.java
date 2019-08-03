package us.rabtrade.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link RacingCompanySearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class RacingCompanySearchRepositoryMockConfiguration {

    @MockBean
    private RacingCompanySearchRepository mockRacingCompanySearchRepository;

}
