package us.mudflap.web.rest;

import us.mudflap.MudflapTruckstopWebApp;
import us.mudflap.domain.TruckStop;
import us.mudflap.domain.User;
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

    private static final Float DEFAULT_BASE_PRICE = 0F;
    private static final Float UPDATED_BASE_PRICE = 1F;

    private static final Float DEFAULT_OPIS_PRICE = 0F;
    private static final Float UPDATED_OPIS_PRICE = 1F;

    private static final String DEFAULT_STREET = "AAAAAAAAAA";
    private static final String UPDATED_STREET = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_MUDFLAP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_MUDFLAP_CODE = "BBBBBBBBBB";

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
            .name(DEFAULT_NAME)
            .basePrice(DEFAULT_BASE_PRICE)
            .opisPrice(DEFAULT_OPIS_PRICE)
            .street(DEFAULT_STREET)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .zipCode(DEFAULT_ZIP_CODE)
            .mudflapCode(DEFAULT_MUDFLAP_CODE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        truckStop.setOwner(user);
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
            .name(UPDATED_NAME)
            .basePrice(UPDATED_BASE_PRICE)
            .opisPrice(UPDATED_OPIS_PRICE)
            .street(UPDATED_STREET)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .mudflapCode(UPDATED_MUDFLAP_CODE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        truckStop.setOwner(user);
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
        assertThat(testTruckStop.getBasePrice()).isEqualTo(DEFAULT_BASE_PRICE);
        assertThat(testTruckStop.getOpisPrice()).isEqualTo(DEFAULT_OPIS_PRICE);
        assertThat(testTruckStop.getStreet()).isEqualTo(DEFAULT_STREET);
        assertThat(testTruckStop.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testTruckStop.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testTruckStop.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);
        assertThat(testTruckStop.getMudflapCode()).isEqualTo(DEFAULT_MUDFLAP_CODE);

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
    public void checkBasePriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = truckStopRepository.findAll().size();
        // set the field null
        truckStop.setBasePrice(null);

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
    public void checkOpisPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = truckStopRepository.findAll().size();
        // set the field null
        truckStop.setOpisPrice(null);

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
    public void checkStreetIsRequired() throws Exception {
        int databaseSizeBeforeTest = truckStopRepository.findAll().size();
        // set the field null
        truckStop.setStreet(null);

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
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = truckStopRepository.findAll().size();
        // set the field null
        truckStop.setCity(null);

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
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = truckStopRepository.findAll().size();
        // set the field null
        truckStop.setState(null);

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
    public void checkZipCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = truckStopRepository.findAll().size();
        // set the field null
        truckStop.setZipCode(null);

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
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].basePrice").value(hasItem(DEFAULT_BASE_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].opisPrice").value(hasItem(DEFAULT_OPIS_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE.toString())))
            .andExpect(jsonPath("$.[*].mudflapCode").value(hasItem(DEFAULT_MUDFLAP_CODE.toString())));
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
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.basePrice").value(DEFAULT_BASE_PRICE.doubleValue()))
            .andExpect(jsonPath("$.opisPrice").value(DEFAULT_OPIS_PRICE.doubleValue()))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE.toString()))
            .andExpect(jsonPath("$.mudflapCode").value(DEFAULT_MUDFLAP_CODE.toString()));
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

    @Test
    @Transactional
    public void getAllTruckStopsByBasePriceIsEqualToSomething() throws Exception {
        // Initialize the database
        truckStopRepository.saveAndFlush(truckStop);

        // Get all the truckStopList where basePrice equals to DEFAULT_BASE_PRICE
        defaultTruckStopShouldBeFound("basePrice.equals=" + DEFAULT_BASE_PRICE);

        // Get all the truckStopList where basePrice equals to UPDATED_BASE_PRICE
        defaultTruckStopShouldNotBeFound("basePrice.equals=" + UPDATED_BASE_PRICE);
    }

    @Test
    @Transactional
    public void getAllTruckStopsByBasePriceIsInShouldWork() throws Exception {
        // Initialize the database
        truckStopRepository.saveAndFlush(truckStop);

        // Get all the truckStopList where basePrice in DEFAULT_BASE_PRICE or UPDATED_BASE_PRICE
        defaultTruckStopShouldBeFound("basePrice.in=" + DEFAULT_BASE_PRICE + "," + UPDATED_BASE_PRICE);

        // Get all the truckStopList where basePrice equals to UPDATED_BASE_PRICE
        defaultTruckStopShouldNotBeFound("basePrice.in=" + UPDATED_BASE_PRICE);
    }

    @Test
    @Transactional
    public void getAllTruckStopsByBasePriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        truckStopRepository.saveAndFlush(truckStop);

        // Get all the truckStopList where basePrice is not null
        defaultTruckStopShouldBeFound("basePrice.specified=true");

        // Get all the truckStopList where basePrice is null
        defaultTruckStopShouldNotBeFound("basePrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllTruckStopsByOpisPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        truckStopRepository.saveAndFlush(truckStop);

        // Get all the truckStopList where opisPrice equals to DEFAULT_OPIS_PRICE
        defaultTruckStopShouldBeFound("opisPrice.equals=" + DEFAULT_OPIS_PRICE);

        // Get all the truckStopList where opisPrice equals to UPDATED_OPIS_PRICE
        defaultTruckStopShouldNotBeFound("opisPrice.equals=" + UPDATED_OPIS_PRICE);
    }

    @Test
    @Transactional
    public void getAllTruckStopsByOpisPriceIsInShouldWork() throws Exception {
        // Initialize the database
        truckStopRepository.saveAndFlush(truckStop);

        // Get all the truckStopList where opisPrice in DEFAULT_OPIS_PRICE or UPDATED_OPIS_PRICE
        defaultTruckStopShouldBeFound("opisPrice.in=" + DEFAULT_OPIS_PRICE + "," + UPDATED_OPIS_PRICE);

        // Get all the truckStopList where opisPrice equals to UPDATED_OPIS_PRICE
        defaultTruckStopShouldNotBeFound("opisPrice.in=" + UPDATED_OPIS_PRICE);
    }

    @Test
    @Transactional
    public void getAllTruckStopsByOpisPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        truckStopRepository.saveAndFlush(truckStop);

        // Get all the truckStopList where opisPrice is not null
        defaultTruckStopShouldBeFound("opisPrice.specified=true");

        // Get all the truckStopList where opisPrice is null
        defaultTruckStopShouldNotBeFound("opisPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllTruckStopsByStreetIsEqualToSomething() throws Exception {
        // Initialize the database
        truckStopRepository.saveAndFlush(truckStop);

        // Get all the truckStopList where street equals to DEFAULT_STREET
        defaultTruckStopShouldBeFound("street.equals=" + DEFAULT_STREET);

        // Get all the truckStopList where street equals to UPDATED_STREET
        defaultTruckStopShouldNotBeFound("street.equals=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    public void getAllTruckStopsByStreetIsInShouldWork() throws Exception {
        // Initialize the database
        truckStopRepository.saveAndFlush(truckStop);

        // Get all the truckStopList where street in DEFAULT_STREET or UPDATED_STREET
        defaultTruckStopShouldBeFound("street.in=" + DEFAULT_STREET + "," + UPDATED_STREET);

        // Get all the truckStopList where street equals to UPDATED_STREET
        defaultTruckStopShouldNotBeFound("street.in=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    public void getAllTruckStopsByStreetIsNullOrNotNull() throws Exception {
        // Initialize the database
        truckStopRepository.saveAndFlush(truckStop);

        // Get all the truckStopList where street is not null
        defaultTruckStopShouldBeFound("street.specified=true");

        // Get all the truckStopList where street is null
        defaultTruckStopShouldNotBeFound("street.specified=false");
    }

    @Test
    @Transactional
    public void getAllTruckStopsByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        truckStopRepository.saveAndFlush(truckStop);

        // Get all the truckStopList where city equals to DEFAULT_CITY
        defaultTruckStopShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the truckStopList where city equals to UPDATED_CITY
        defaultTruckStopShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllTruckStopsByCityIsInShouldWork() throws Exception {
        // Initialize the database
        truckStopRepository.saveAndFlush(truckStop);

        // Get all the truckStopList where city in DEFAULT_CITY or UPDATED_CITY
        defaultTruckStopShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the truckStopList where city equals to UPDATED_CITY
        defaultTruckStopShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllTruckStopsByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        truckStopRepository.saveAndFlush(truckStop);

        // Get all the truckStopList where city is not null
        defaultTruckStopShouldBeFound("city.specified=true");

        // Get all the truckStopList where city is null
        defaultTruckStopShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    public void getAllTruckStopsByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        truckStopRepository.saveAndFlush(truckStop);

        // Get all the truckStopList where state equals to DEFAULT_STATE
        defaultTruckStopShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the truckStopList where state equals to UPDATED_STATE
        defaultTruckStopShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllTruckStopsByStateIsInShouldWork() throws Exception {
        // Initialize the database
        truckStopRepository.saveAndFlush(truckStop);

        // Get all the truckStopList where state in DEFAULT_STATE or UPDATED_STATE
        defaultTruckStopShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the truckStopList where state equals to UPDATED_STATE
        defaultTruckStopShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllTruckStopsByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        truckStopRepository.saveAndFlush(truckStop);

        // Get all the truckStopList where state is not null
        defaultTruckStopShouldBeFound("state.specified=true");

        // Get all the truckStopList where state is null
        defaultTruckStopShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    public void getAllTruckStopsByZipCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        truckStopRepository.saveAndFlush(truckStop);

        // Get all the truckStopList where zipCode equals to DEFAULT_ZIP_CODE
        defaultTruckStopShouldBeFound("zipCode.equals=" + DEFAULT_ZIP_CODE);

        // Get all the truckStopList where zipCode equals to UPDATED_ZIP_CODE
        defaultTruckStopShouldNotBeFound("zipCode.equals=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    public void getAllTruckStopsByZipCodeIsInShouldWork() throws Exception {
        // Initialize the database
        truckStopRepository.saveAndFlush(truckStop);

        // Get all the truckStopList where zipCode in DEFAULT_ZIP_CODE or UPDATED_ZIP_CODE
        defaultTruckStopShouldBeFound("zipCode.in=" + DEFAULT_ZIP_CODE + "," + UPDATED_ZIP_CODE);

        // Get all the truckStopList where zipCode equals to UPDATED_ZIP_CODE
        defaultTruckStopShouldNotBeFound("zipCode.in=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    public void getAllTruckStopsByZipCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        truckStopRepository.saveAndFlush(truckStop);

        // Get all the truckStopList where zipCode is not null
        defaultTruckStopShouldBeFound("zipCode.specified=true");

        // Get all the truckStopList where zipCode is null
        defaultTruckStopShouldNotBeFound("zipCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllTruckStopsByMudflapCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        truckStopRepository.saveAndFlush(truckStop);

        // Get all the truckStopList where mudflapCode equals to DEFAULT_MUDFLAP_CODE
        defaultTruckStopShouldBeFound("mudflapCode.equals=" + DEFAULT_MUDFLAP_CODE);

        // Get all the truckStopList where mudflapCode equals to UPDATED_MUDFLAP_CODE
        defaultTruckStopShouldNotBeFound("mudflapCode.equals=" + UPDATED_MUDFLAP_CODE);
    }

    @Test
    @Transactional
    public void getAllTruckStopsByMudflapCodeIsInShouldWork() throws Exception {
        // Initialize the database
        truckStopRepository.saveAndFlush(truckStop);

        // Get all the truckStopList where mudflapCode in DEFAULT_MUDFLAP_CODE or UPDATED_MUDFLAP_CODE
        defaultTruckStopShouldBeFound("mudflapCode.in=" + DEFAULT_MUDFLAP_CODE + "," + UPDATED_MUDFLAP_CODE);

        // Get all the truckStopList where mudflapCode equals to UPDATED_MUDFLAP_CODE
        defaultTruckStopShouldNotBeFound("mudflapCode.in=" + UPDATED_MUDFLAP_CODE);
    }

    @Test
    @Transactional
    public void getAllTruckStopsByMudflapCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        truckStopRepository.saveAndFlush(truckStop);

        // Get all the truckStopList where mudflapCode is not null
        defaultTruckStopShouldBeFound("mudflapCode.specified=true");

        // Get all the truckStopList where mudflapCode is null
        defaultTruckStopShouldNotBeFound("mudflapCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllTruckStopsByOwnerIsEqualToSomething() throws Exception {
        // Get already existing entity
        User owner = truckStop.getOwner();
        truckStopRepository.saveAndFlush(truckStop);
        Long ownerId = owner.getId();

        // Get all the truckStopList where owner equals to ownerId
        defaultTruckStopShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the truckStopList where owner equals to ownerId + 1
        defaultTruckStopShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTruckStopShouldBeFound(String filter) throws Exception {
        restTruckStopMockMvc.perform(get("/api/truck-stops?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(truckStop.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].basePrice").value(hasItem(DEFAULT_BASE_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].opisPrice").value(hasItem(DEFAULT_OPIS_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].mudflapCode").value(hasItem(DEFAULT_MUDFLAP_CODE)));

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
            .name(UPDATED_NAME)
            .basePrice(UPDATED_BASE_PRICE)
            .opisPrice(UPDATED_OPIS_PRICE)
            .street(UPDATED_STREET)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .mudflapCode(UPDATED_MUDFLAP_CODE);
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
        assertThat(testTruckStop.getBasePrice()).isEqualTo(UPDATED_BASE_PRICE);
        assertThat(testTruckStop.getOpisPrice()).isEqualTo(UPDATED_OPIS_PRICE);
        assertThat(testTruckStop.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testTruckStop.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testTruckStop.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testTruckStop.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testTruckStop.getMudflapCode()).isEqualTo(UPDATED_MUDFLAP_CODE);

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
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].basePrice").value(hasItem(DEFAULT_BASE_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].opisPrice").value(hasItem(DEFAULT_OPIS_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].mudflapCode").value(hasItem(DEFAULT_MUDFLAP_CODE)));
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
