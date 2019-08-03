package us.rabtrade.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import us.rabtrade.domain.TruckStop;
import us.rabtrade.domain.*; // for static metamodels
import us.rabtrade.repository.TruckStopRepository;
import us.rabtrade.repository.search.TruckStopSearchRepository;
import us.rabtrade.service.dto.TruckStopCriteria;
import us.rabtrade.service.dto.TruckStopDTO;
import us.rabtrade.service.mapper.TruckStopMapper;

/**
 * Service for executing complex queries for {@link TruckStop} entities in the database.
 * The main input is a {@link TruckStopCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TruckStopDTO} or a {@link Page} of {@link TruckStopDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TruckStopQueryService extends QueryService<TruckStop> {

    private final Logger log = LoggerFactory.getLogger(TruckStopQueryService.class);

    private final TruckStopRepository truckStopRepository;

    private final TruckStopMapper truckStopMapper;

    private final TruckStopSearchRepository truckStopSearchRepository;

    public TruckStopQueryService(TruckStopRepository truckStopRepository, TruckStopMapper truckStopMapper, TruckStopSearchRepository truckStopSearchRepository) {
        this.truckStopRepository = truckStopRepository;
        this.truckStopMapper = truckStopMapper;
        this.truckStopSearchRepository = truckStopSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TruckStopDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TruckStopDTO> findByCriteria(TruckStopCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TruckStop> specification = createSpecification(criteria);
        return truckStopMapper.toDto(truckStopRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TruckStopDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TruckStopDTO> findByCriteria(TruckStopCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TruckStop> specification = createSpecification(criteria);
        return truckStopRepository.findAll(specification, page)
            .map(truckStopMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TruckStopCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TruckStop> specification = createSpecification(criteria);
        return truckStopRepository.count(specification);
    }

    /**
     * Function to convert ConsumerCriteria to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */    
    private Specification<TruckStop> createSpecification(TruckStopCriteria criteria) {
        Specification<TruckStop> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), TruckStop_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), TruckStop_.name));
            }
            if (criteria.getBasePrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBasePrice(), TruckStop_.basePrice));
            }
            if (criteria.getOpisPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOpisPrice(), TruckStop_.opisPrice));
            }
            if (criteria.getStreet() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStreet(), TruckStop_.street));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), TruckStop_.city));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildStringSpecification(criteria.getState(), TruckStop_.state));
            }
            if (criteria.getZipCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getZipCode(), TruckStop_.zipCode));
            }
            if (criteria.getRacingCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRacingCode(), TruckStop_.racingCode));
            }
            if (criteria.getOwnerId() != null) {
                specification = specification.and(buildSpecification(criteria.getOwnerId(),
                    root -> root.join(TruckStop_.owner, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
