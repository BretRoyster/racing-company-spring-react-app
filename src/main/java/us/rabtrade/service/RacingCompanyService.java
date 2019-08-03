package us.rabtrade.service;

import us.rabtrade.domain.RacingCompany;
import us.rabtrade.repository.RacingCompanyRepository;
import us.rabtrade.repository.search.RacingCompanySearchRepository;
import us.rabtrade.service.dto.RacingCompanyDTO;
import us.rabtrade.service.mapper.RacingCompanyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link RacingCompany}.
 */
@Service
@Transactional
public class RacingCompanyService {

    private final Logger log = LoggerFactory.getLogger(RacingCompanyService.class);

    private final RacingCompanyRepository racingCompanyRepository;

    private final RacingCompanyMapper racingCompanyMapper;

    private final RacingCompanySearchRepository racingCompanySearchRepository;

    public RacingCompanyService(RacingCompanyRepository racingCompanyRepository, RacingCompanyMapper racingCompanyMapper, RacingCompanySearchRepository racingCompanySearchRepository) {
        this.racingCompanyRepository = racingCompanyRepository;
        this.racingCompanyMapper = racingCompanyMapper;
        this.racingCompanySearchRepository = racingCompanySearchRepository;
    }

    /**
     * Save a racingCompany.
     *
     * @param racingCompanyDTO the entity to save.
     * @return the persisted entity.
     */
    public RacingCompanyDTO save(RacingCompanyDTO racingCompanyDTO) {
        log.debug("Request to save RacingCompany : {}", racingCompanyDTO);
        RacingCompany racingCompany = racingCompanyMapper.toEntity(racingCompanyDTO);
        racingCompany = racingCompanyRepository.save(racingCompany);
        RacingCompanyDTO result = racingCompanyMapper.toDto(racingCompany);
        racingCompanySearchRepository.save(racingCompany);
        return result;
    }

    /**
     * Get all the racingCompanies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RacingCompanyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RacingCompanies");
        return racingCompanyRepository.findAll(pageable)
            .map(racingCompanyMapper::toDto);
    }


    /**
     * Get one racingCompany by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RacingCompanyDTO> findOne(Long id) {
        log.debug("Request to get RacingCompany : {}", id);
        return racingCompanyRepository.findById(id)
            .map(racingCompanyMapper::toDto);
    }

    /**
     * Delete the racingCompany by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RacingCompany : {}", id);
        racingCompanyRepository.deleteById(id);
        racingCompanySearchRepository.deleteById(id);
    }

    /**
     * Search for the racingCompany corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RacingCompanyDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RacingCompanies for query {}", query);
        return racingCompanySearchRepository.search(queryStringQuery(query), pageable)
            .map(racingCompanyMapper::toDto);
    }
}
