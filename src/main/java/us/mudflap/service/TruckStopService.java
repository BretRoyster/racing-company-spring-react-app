package us.mudflap.service;

import us.mudflap.domain.TruckStop;
import us.mudflap.repository.TruckStopRepository;
import us.mudflap.repository.search.TruckStopSearchRepository;
import us.mudflap.service.dto.TruckStopDTO;
import us.mudflap.service.mapper.TruckStopMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link TruckStop}.
 */
@Service
@Transactional
public class TruckStopService {

    private final Logger log = LoggerFactory.getLogger(TruckStopService.class);

    private final TruckStopRepository truckStopRepository;

    private final TruckStopMapper truckStopMapper;

    private final TruckStopSearchRepository truckStopSearchRepository;

    public TruckStopService(TruckStopRepository truckStopRepository, TruckStopMapper truckStopMapper, TruckStopSearchRepository truckStopSearchRepository) {
        this.truckStopRepository = truckStopRepository;
        this.truckStopMapper = truckStopMapper;
        this.truckStopSearchRepository = truckStopSearchRepository;
    }

    /**
     * Save a truckStop.
     *
     * @param truckStopDTO the entity to save.
     * @return the persisted entity.
     */
    public TruckStopDTO save(TruckStopDTO truckStopDTO) {
        log.debug("Request to save TruckStop : {}", truckStopDTO);
        TruckStop truckStop = truckStopMapper.toEntity(truckStopDTO);
        truckStop = truckStopRepository.save(truckStop);
        TruckStopDTO result = truckStopMapper.toDto(truckStop);
        truckStopSearchRepository.save(truckStop);
        return result;
    }

    /**
     * Get all the truckStops.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TruckStopDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TruckStops");
        return truckStopRepository.findAll(pageable)
            .map(truckStopMapper::toDto);
    }


    /**
     * Get one truckStop by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TruckStopDTO> findOne(Long id) {
        log.debug("Request to get TruckStop : {}", id);
        return truckStopRepository.findById(id)
            .map(truckStopMapper::toDto);
    }

    /**
     * Delete the truckStop by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TruckStop : {}", id);
        truckStopRepository.deleteById(id);
        truckStopSearchRepository.deleteById(id);
    }

    /**
     * Search for the truckStop corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TruckStopDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TruckStops for query {}", query);
        return truckStopSearchRepository.search(queryStringQuery(query), pageable)
            .map(truckStopMapper::toDto);
    }
}
