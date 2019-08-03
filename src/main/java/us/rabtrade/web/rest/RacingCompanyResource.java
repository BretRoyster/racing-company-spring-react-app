package us.rabtrade.web.rest;

import us.rabtrade.service.RacingCompanyService;
import us.rabtrade.web.rest.errors.BadRequestAlertException;
import us.rabtrade.service.dto.RacingCompanyDTO;
import us.rabtrade.service.dto.RacingCompanyCriteria;
import us.rabtrade.service.RacingCompanyQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link us.rabtrade.domain.RacingCompany}.
 */
@RestController
@RequestMapping("/api")
public class RacingCompanyResource {

    private final Logger log = LoggerFactory.getLogger(RacingCompanyResource.class);

    private static final String ENTITY_NAME = "racingCompany";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RacingCompanyService racingCompanyService;

    private final RacingCompanyQueryService racingCompanyQueryService;

    public RacingCompanyResource(RacingCompanyService racingCompanyService, RacingCompanyQueryService racingCompanyQueryService) {
        this.racingCompanyService = racingCompanyService;
        this.racingCompanyQueryService = racingCompanyQueryService;
    }

    /**
     * {@code POST  /racing-companies} : Create a new racingCompany.
     *
     * @param racingCompanyDTO the racingCompanyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new racingCompanyDTO, or with status {@code 400 (Bad Request)} if the racingCompany has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/racing-companies")
    public ResponseEntity<RacingCompanyDTO> createRacingCompany(@Valid @RequestBody RacingCompanyDTO racingCompanyDTO) throws URISyntaxException {
        log.debug("REST request to save RacingCompany : {}", racingCompanyDTO);
        if (racingCompanyDTO.getId() != null) {
            throw new BadRequestAlertException("A new racingCompany cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RacingCompanyDTO result = racingCompanyService.save(racingCompanyDTO);
        result.setId(1L); // For Demo...
        return ResponseEntity.created(new URI("/api/racing-companies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /racing-companies} : Updates an existing racingCompany.
     *
     * @param racingCompanyDTO the racingCompanyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated racingCompanyDTO,
     * or with status {@code 400 (Bad Request)} if the racingCompanyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the racingCompanyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/racing-companies")
    public ResponseEntity<RacingCompanyDTO> updateRacingCompany(@Valid @RequestBody RacingCompanyDTO racingCompanyDTO) throws URISyntaxException {
        log.debug("REST request to update RacingCompany : {}", racingCompanyDTO);
        if (racingCompanyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RacingCompanyDTO result = racingCompanyService.save(racingCompanyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, racingCompanyDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /racing-companies} : get all the racingCompanies.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of racingCompanies in body.
     */
    @GetMapping("/racing-companies")
    public ResponseEntity<List<RacingCompanyDTO>> getAllRacingCompanies(RacingCompanyCriteria criteria, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get RacingCompanies by criteria: {}", criteria);
        Page<RacingCompanyDTO> page = racingCompanyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /racing-companies/count} : count all the racingCompanies.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/racing-companies/count")
    public ResponseEntity<Long> countRacingCompanies(RacingCompanyCriteria criteria) {
        log.debug("REST request to count RacingCompanies by criteria: {}", criteria);
        return ResponseEntity.ok().body(racingCompanyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /racing-companies/:id} : get the "id" racingCompany.
     *
     * @param id the id of the racingCompanyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the racingCompanyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/racing-companies/{id}")
    public ResponseEntity<RacingCompanyDTO> getRacingCompany(@PathVariable Long id) {
        log.debug("REST request to get RacingCompany : {}", id);
        Optional<RacingCompanyDTO> racingCompanyDTO = racingCompanyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(racingCompanyDTO);
    }

    /**
     * {@code DELETE  /racing-companies/:id} : delete the "id" racingCompany.
     *
     * @param id the id of the racingCompanyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/racing-companies/{id}")
    public ResponseEntity<Void> deleteRacingCompany(@PathVariable Long id) {
        log.debug("REST request to delete RacingCompany : {}", id);
        racingCompanyService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/racing-companies?query=:query} : search for the racingCompany corresponding
     * to the query.
     *
     * @param query the query of the racingCompany search.
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the result of the search.
     */
    @GetMapping("/_search/racing-companies")
    public ResponseEntity<List<RacingCompanyDTO>> searchRacingCompanies(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of RacingCompanies for query {}", query);
        Page<RacingCompanyDTO> page = racingCompanyService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
