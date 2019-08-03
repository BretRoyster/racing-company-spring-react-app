package us.rabtrade.web.rest;

import us.rabtrade.service.TruckStopService;
import us.rabtrade.web.rest.errors.BadRequestAlertException;
import us.rabtrade.service.dto.TruckStopDTO;
import us.rabtrade.service.dto.TruckStopCriteria;
import us.rabtrade.service.TruckStopQueryService;

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
 * REST controller for managing {@link us.rabtrade.domain.TruckStop}.
 */
@RestController
@RequestMapping("/api")
public class TruckStopResource {

    private final Logger log = LoggerFactory.getLogger(TruckStopResource.class);

    private static final String ENTITY_NAME = "truckStop";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TruckStopService truckStopService;

    private final TruckStopQueryService truckStopQueryService;

    public TruckStopResource(TruckStopService truckStopService, TruckStopQueryService truckStopQueryService) {
        this.truckStopService = truckStopService;
        this.truckStopQueryService = truckStopQueryService;
    }

    /**
     * {@code POST  /truck-stops} : Create a new truckStop.
     *
     * @param truckStopDTO the truckStopDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new truckStopDTO, or with status {@code 400 (Bad Request)} if the truckStop has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/truck-stops")
    public ResponseEntity<TruckStopDTO> createTruckStop(@Valid @RequestBody TruckStopDTO truckStopDTO) throws URISyntaxException {
        log.debug("REST request to save TruckStop : {}", truckStopDTO);
        if (truckStopDTO.getId() != null) {
            throw new BadRequestAlertException("A new truckStop cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TruckStopDTO result = truckStopService.save(truckStopDTO);
        return ResponseEntity.created(new URI("/api/truck-stops/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /truck-stops} : Updates an existing truckStop.
     *
     * @param truckStopDTO the truckStopDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated truckStopDTO,
     * or with status {@code 400 (Bad Request)} if the truckStopDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the truckStopDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/truck-stops")
    public ResponseEntity<TruckStopDTO> updateTruckStop(@Valid @RequestBody TruckStopDTO truckStopDTO) throws URISyntaxException {
        log.debug("REST request to update TruckStop : {}", truckStopDTO);
        if (truckStopDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TruckStopDTO result = truckStopService.save(truckStopDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, truckStopDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /truck-stops} : get all the truckStops.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of truckStops in body.
     */
    @GetMapping("/truck-stops")
    public ResponseEntity<List<TruckStopDTO>> getAllTruckStops(TruckStopCriteria criteria, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get TruckStops by criteria: {}", criteria);
        Page<TruckStopDTO> page = truckStopQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /truck-stops/count} : count all the truckStops.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/truck-stops/count")
    public ResponseEntity<Long> countTruckStops(TruckStopCriteria criteria) {
        log.debug("REST request to count TruckStops by criteria: {}", criteria);
        return ResponseEntity.ok().body(truckStopQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /truck-stops/:id} : get the "id" truckStop.
     *
     * @param id the id of the truckStopDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the truckStopDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/truck-stops/{id}")
    public ResponseEntity<TruckStopDTO> getTruckStop(@PathVariable Long id) {
        log.debug("REST request to get TruckStop : {}", id);
        Optional<TruckStopDTO> truckStopDTO = truckStopService.findOne(id);
        return ResponseUtil.wrapOrNotFound(truckStopDTO);
    }

    /**
     * {@code DELETE  /truck-stops/:id} : delete the "id" truckStop.
     *
     * @param id the id of the truckStopDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/truck-stops/{id}")
    public ResponseEntity<Void> deleteTruckStop(@PathVariable Long id) {
        log.debug("REST request to delete TruckStop : {}", id);
        truckStopService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/truck-stops?query=:query} : search for the truckStop corresponding
     * to the query.
     *
     * @param query the query of the truckStop search.
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the result of the search.
     */
    @GetMapping("/_search/truck-stops")
    public ResponseEntity<List<TruckStopDTO>> searchTruckStops(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of TruckStops for query {}", query);
        Page<TruckStopDTO> page = truckStopService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
