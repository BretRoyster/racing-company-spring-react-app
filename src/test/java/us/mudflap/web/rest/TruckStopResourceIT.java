package us.mudflap.web.rest;

import us.mudflap.MudflapTruckstopWebApp;
import us.mudflap.domain.TruckStop;
import us.mudflap.repository.TruckStopRepository;
import us.mudflap.repository.search.TruckStopSearchRepository;
import us.mudflap.service.TruckStopService;
import us.mudflap.service.dto.TruckStopDTO;
import us.mudflap.service.mapper.TruckStopMapper;
import us.mudflap.web.rest.errors.ExceptionTranslator;
import us.mudflap.service.dto.TruckStopCriteria;
import us.mudflap.service.TruckStopQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static us.mudflap.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link TruckStopResource} REST controller.
 */
@SpringBootTest(classes = MudflapTruckstopWebApp.class)
public class TruckStopResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private TruckStopRepository truckStopRepository;

    @Autowired
    private TruckStopMapper truckStopMapper;

    @Autowired
    private TruckStopService truckStopService;

    /**
     * This repository is mocked in the us.mudflap.repository.search test package.
     *
     * @see us.mudflap.repository.search.TruckStopSearchRepositoryMockConfiguration
     */
    @Autowired
    private TruckStopSearchRepository mockTruckStopSearchRepository;

    @Autowired
    private TruckStopQueryService truckStopQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restTruckStopMockMvc;

    private TruckStop truckStop;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TruckStopResource truckStopResource = new TruckStopResource(truckStopService, truckStopQueryService);
        this.restTruckStopMockMvc = MockMvcBuilders.standaloneSetup(truckStopResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TruckStop createEntity(EntityManager em) {
        TruckStop truckStop = new TruckStop()
            .name(DEFAULT_NAME);
        return truckStop;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TruckStop createUpdatedEntity(EntityManager em) {
        TruckStop truckStop = new TruckStop()
            .name(UPDATED_NAME);
        return truckStop;
    }

    @BeforeEach
    public void initTest() {
        truckStop = createEntity(em);
    }

    @Test
    @Transactional
    public void createTruckStop() throws Exception {
        int databaseSizeBeforeCreate = truckStopRepository.findAll().size();

        // Create the TruckStop
        TruckStopDTO truckStopDTO = truckStopMapper.toDto(truckStop);
        restTruckStopMockMvc.perform(post("/api/truck-stops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(truckStopDTO)))
            .andExpect(status().isCreated());

        // Validate the TruckStop in the database
        List<TruckStop> truckStopList = truckStopRepository.findAll();
        assertThat(truckStopList).hasSize(databaseSizeBeforeCreate + 1);
        TruckStop testTruckStop = truckStopList.get(truckStopList.size() - 1);
        assertThat(testTruckStop.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the TruckStop in Elasticsearch
        verify(mockTruckStopSearchRepository, times(1)).save(testTruckStop);
    }

    @Test
    @Transactional
    public void createTruckStopWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = truckStopRepository.findAll().size();

        // Create the TruckStop with an existing ID
        truckStop.setId(1L);
        TruckStopDTO truckStopDTO = truckStopMapper.toDto(truckStop);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTruckStopMockMvc.perform(post("/api/truck-stops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(truckStopDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TruckStop in the database
        List<TruckStop> truckStopList = truckStopRepository.findAll();
        assertThat(truckStopList).hasSize(databaseSizeBeforeCreate);

        // Validate the TruckStop in Elasticsearch
        verify(mockTruckStopSearchRepository, times(0)).save(truckStop);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = truckStopRepository.findAll().size();
        // set the field null
        truckStop.setName(null);

        // Create the TruckStop, which fails.
        TruckStopDTO truckStopDTO = truckStopMapper.toDto(truckStop);

        restTruckStopMockMvc.perform(post("/api/truck-stops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(truckStopDTO)))
            .andExpect(status().isBadRequest());

        List<TruckStop> truckStopList = truckStopRepository.findAll();
        assertThat(truckStopList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTruckStops() throws Exception {
        // Initialize the database
        truckStopRepository.saveAndFlush(truckStop);

        // Get all the truckStopList
        restTruckStopMockMvc.perform(get("/api/truck-stops?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(truckStop.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getTruckStop() throws Exception {
        // Initialize the database
        truckStopRepository.saveAndFlush(truckStop);

        // Get the truckStop
        restTruckStopMockMvc.perform(get("/api/truck-stops/{id}", truckStop.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(truckStop.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllTruckStopsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        truckStopRepository.saveAndFlush(truckStop);

        // Get all the truckStopList where name equals to DEFAULT_NAME
        defaultTruckStopShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the truckStopList where name equals to UPDATED_NAME
        defaultTruckStopShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTruckStopsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        truckStopRepository.saveAndFlush(truckStop);

        // Get all the truckStopList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTruckStopShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the truckStopList where name equals to UPDATED_NAME
        defaultTruckStopShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTruckStopsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        truckStopRepository.saveAndFlush(truckStop);

        // Get all the truckStopList where name is not null
        defaultTruckStopShouldBeFound("name.specified=true");

        // Get all the truckStopList where name is null
        defaultTruckStopShouldNotBeFound("name.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTruckStopShouldBeFound(String filter) throws Exception {
        restTruckStopMockMvc.perform(get("/api/truck-stops?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(truckStop.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restTruckStopMockMvc.perform(get("/api/truck-stops/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTruckStopShouldNotBeFound(String filter) throws Exception {
        restTruckStopMockMvc.perform(get("/api/truck-stops?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTruckStopMockMvc.perform(get("/api/truck-stops/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTruckStop() throws Exception {
        // Get the truckStop
        restTruckStopMockMvc.perform(get("/api/truck-stops/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTruckStop() throws Exception {
        // Initialize the database
        truckStopRepository.saveAndFlush(truckStop);

        int databaseSizeBeforeUpdate = truckStopRepository.findAll().size();

        // Update the truckStop
        TruckStop updatedTruckStop = truckStopRepository.findById(truckStop.getId()).get();
        // Disconnect from session so that the updates on updatedTruckStop are not directly saved in db
        em.detach(updatedTruckStop);
        updatedTruckStop
            .name(UPDATED_NAME);
        TruckStopDTO truckStopDTO = truckStopMapper.toDto(updatedTruckStop);

        restTruckStopMockMvc.perform(put("/api/truck-stops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(truckStopDTO)))
            .andExpect(status().isOk());

        // Validate the TruckStop in the database
        List<TruckStop> truckStopList = truckStopRepository.findAll();
        assertThat(truckStopList).hasSize(databaseSizeBeforeUpdate);
        TruckStop testTruckStop = truckStopList.get(truckStopList.size() - 1);
        assertThat(testTruckStop.getName()).isEqualTo(UPDATED_NAME);

        // Validate the TruckStop in Elasticsearch
        verify(mockTruckStopSearchRepository, times(1)).save(testTruckStop);
    }

    @Test
    @Transactional
    public void updateNonExistingTruckStop() throws Exception {
        int databaseSizeBeforeUpdate = truckStopRepository.findAll().size();

        // Create the TruckStop
        TruckStopDTO truckStopDTO = truckStopMapper.toDto(truckStop);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTruckStopMockMvc.perform(put("/api/truck-stops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(truckStopDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TruckStop in the database
        List<TruckStop> truckStopList = truckStopRepository.findAll();
        assertThat(truckStopList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TruckStop in Elasticsearch
        verify(mockTruckStopSearchRepository, times(0)).save(truckStop);
    }

    @Test
    @Transactional
    public void deleteTruckStop() throws Exception {
        // Initialize the database
        truckStopRepository.saveAndFlush(truckStop);

        int databaseSizeBeforeDelete = truckStopRepository.findAll().size();

        // Delete the truckStop
        restTruckStopMockMvc.perform(delete("/api/truck-stops/{id}", truckStop.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TruckStop> truckStopList = truckStopRepository.findAll();
        assertThat(truckStopList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TruckStop in Elasticsearch
        verify(mockTruckStopSearchRepository, times(1)).deleteById(truckStop.getId());
    }

    @Test
    @Transactional
    public void searchTruckStop() throws Exception {
        // Initialize the database
        truckStopRepository.saveAndFlush(truckStop);
        when(mockTruckStopSearchRepository.search(queryStringQuery("id:" + truckStop.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(truckStop), PageRequest.of(0, 1), 1));
        // Search the truckStop
        restTruckStopMockMvc.perform(get("/api/_search/truck-stops?query=id:" + truckStop.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(truckStop.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TruckStop.class);
        TruckStop truckStop1 = new TruckStop();
        truckStop1.setId(1L);
        TruckStop truckStop2 = new TruckStop();
        truckStop2.setId(truckStop1.getId());
        assertThat(truckStop1).isEqualTo(truckStop2);
        truckStop2.setId(2L);
        assertThat(truckStop1).isNotEqualTo(truckStop2);
        truckStop1.setId(null);
        assertThat(truckStop1).isNotEqualTo(truckStop2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TruckStopDTO.class);
        TruckStopDTO truckStopDTO1 = new TruckStopDTO();
        truckStopDTO1.setId(1L);
        TruckStopDTO truckStopDTO2 = new TruckStopDTO();
        assertThat(truckStopDTO1).isNotEqualTo(truckStopDTO2);
        truckStopDTO2.setId(truckStopDTO1.getId());
        assertThat(truckStopDTO1).isEqualTo(truckStopDTO2);
        truckStopDTO2.setId(2L);
        assertThat(truckStopDTO1).isNotEqualTo(truckStopDTO2);
        truckStopDTO1.setId(null);
        assertThat(truckStopDTO1).isNotEqualTo(truckStopDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(truckStopMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(truckStopMapper.fromId(null)).isNull();
    }
}
