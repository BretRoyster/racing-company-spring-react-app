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

import us.rabtrade.domain.RacingCompany;
import us.rabtrade.domain.*; // for static metamodels
import us.rabtrade.repository.RacingCompanyRepository;
import us.rabtrade.repository.search.RacingCompanySearchRepository;
import us.rabtrade.service.dto.RacingCompanyCriteria;
import us.rabtrade.service.dto.RacingCompanyDTO;
import us.rabtrade.service.mapper.RacingCompanyMapper;

/**
 * Service for executing complex queries for {@link RacingCompany} entities in the database.
 * The main input is a {@link RacingCompanyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RacingCompanyDTO} or a {@link Page} of {@link RacingCompanyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RacingCompanyQueryService extends QueryService<RacingCompany> {

    private final Logger log = LoggerFactory.getLogger(RacingCompanyQueryService.class);

    private final RacingCompanyRepository racingCompanyRepository;

    private final RacingCompanyMapper racingCompanyMapper;

    private final RacingCompanySearchRepository racingCompanySearchRepository;

    public RacingCompanyQueryService(RacingCompanyRepository racingCompanyRepository, RacingCompanyMapper racingCompanyMapper, RacingCompanySearchRepository racingCompanySearchRepository) {
        this.racingCompanyRepository = racingCompanyRepository;
        this.racingCompanyMapper = racingCompanyMapper;
        this.racingCompanySearchRepository = racingCompanySearchRepository;
    }

    /**
     * Return a {@link List} of {@link RacingCompanyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RacingCompanyDTO> findByCriteria(RacingCompanyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RacingCompany> specification = createSpecification(criteria);
        return racingCompanyMapper.toDto(racingCompanyRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RacingCompanyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RacingCompanyDTO> findByCriteria(RacingCompanyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RacingCompany> specification = createSpecification(criteria);
        return racingCompanyRepository.findAll(specification, page)
            .map(racingCompanyMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RacingCompanyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RacingCompany> specification = createSpecification(criteria);
        return racingCompanyRepository.count(specification);
    }

    /**
     * Function to convert ConsumerCriteria to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */    
    private Specification<RacingCompany> createSpecification(RacingCompanyCriteria criteria) {
        Specification<RacingCompany> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), RacingCompany_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), RacingCompany_.name));
            }
            if (criteria.getGasPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGasPrice(), RacingCompany_.gasPrice));
            }
            if (criteria.getServicePrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getServicePrice(), RacingCompany_.servicePrice));
            }
            if (criteria.getStreet() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStreet(), RacingCompany_.street));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), RacingCompany_.city));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildStringSpecification(criteria.getState(), RacingCompany_.state));
            }
            if (criteria.getZipCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getZipCode(), RacingCompany_.zipCode));
            }
            if (criteria.getRacingCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRacingCode(), RacingCompany_.racingCode));
            }
            if (criteria.getOwnerId() != null) {
                specification = specification.and(buildSpecification(criteria.getOwnerId(),
                    root -> root.join(RacingCompany_.owner, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
