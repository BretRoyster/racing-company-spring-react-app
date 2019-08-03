package us.rabtrade.web.rest;

import us.rabtrade.RacingCompanyWebApp;
import us.rabtrade.domain.RacingCompany;
import us.rabtrade.domain.User;
import us.rabtrade.repository.RacingCompanyRepository;
import us.rabtrade.repository.search.RacingCompanySearchRepository;
import us.rabtrade.service.RacingCompanyService;
import us.rabtrade.service.dto.RacingCompanyDTO;
import us.rabtrade.service.mapper.RacingCompanyMapper;
import us.rabtrade.web.rest.errors.ExceptionTranslator;
import us.rabtrade.service.dto.RacingCompanyCriteria;
import us.rabtrade.service.RacingCompanyQueryService;

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

import static us.rabtrade.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link RacingCompanyResource} REST controller.
 */
@SpringBootTest(classes = RacingCompanyWebApp.class)
public class RacingCompanyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Float DEFAULT_GAS_PRICE = 0F;
    private static final Float UPDATED_GAS_PRICE = 1F;

    private static final Float DEFAULT_SERVICE_PRICE = 0F;
    private static final Float UPDATED_SERVICE_PRICE = 1F;

    private static final String DEFAULT_STREET = "AAAAAAAAAA";
    private static final String UPDATED_STREET = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_RACING_CODE = "AAAAAAAAAA";
    private static final String UPDATED_RACING_CODE = "BBBBBBBBBB";

    @Autowired
    private RacingCompanyRepository racingCompanyRepository;

    @Autowired
    private RacingCompanyMapper racingCompanyMapper;

    @Autowired
    private RacingCompanyService racingCompanyService;

    /**
     * This repository is mocked in the us.rabtrade.repository.search test package.
     *
     * @see us.rabtrade.repository.search.RacingCompanySearchRepositoryMockConfiguration
     */
    @Autowired
    private RacingCompanySearchRepository mockRacingCompanySearchRepository;

    @Autowired
    private RacingCompanyQueryService racingCompanyQueryService;

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

    private MockMvc restRacingCompanyMockMvc;

    private RacingCompany racingCompany;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RacingCompanyResource racingCompanyResource = new RacingCompanyResource(racingCompanyService, racingCompanyQueryService);
        this.restRacingCompanyMockMvc = MockMvcBuilders.standaloneSetup(racingCompanyResource)
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
    public static RacingCompany createEntity(EntityManager em) {
        RacingCompany racingCompany = new RacingCompany()
            .name(DEFAULT_NAME)
            .gasPrice(DEFAULT_GAS_PRICE)
            .servicePrice(DEFAULT_SERVICE_PRICE)
            .street(DEFAULT_STREET)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .zipCode(DEFAULT_ZIP_CODE)
            .racingCode(DEFAULT_RACING_CODE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        racingCompany.setOwner(user);
        return racingCompany;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RacingCompany createUpdatedEntity(EntityManager em) {
        RacingCompany racingCompany = new RacingCompany()
            .name(UPDATED_NAME)
            .gasPrice(UPDATED_GAS_PRICE)
            .servicePrice(UPDATED_SERVICE_PRICE)
            .street(UPDATED_STREET)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .racingCode(UPDATED_RACING_CODE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        racingCompany.setOwner(user);
        return racingCompany;
    }

    @BeforeEach
    public void initTest() {
        racingCompany = createEntity(em);
    }

    @Test
    @Transactional
    public void createRacingCompany() throws Exception {
        int databaseSizeBeforeCreate = racingCompanyRepository.findAll().size();

        // Create the RacingCompany
        RacingCompanyDTO racingCompanyDTO = racingCompanyMapper.toDto(racingCompany);
        restRacingCompanyMockMvc.perform(post("/api/racing-companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(racingCompanyDTO)))
            .andExpect(status().isCreated());

        // Validate the RacingCompany in the database
        List<RacingCompany> racingCompanyList = racingCompanyRepository.findAll();
        assertThat(racingCompanyList).hasSize(databaseSizeBeforeCreate + 1);
        RacingCompany testRacingCompany = racingCompanyList.get(racingCompanyList.size() - 1);
        assertThat(testRacingCompany.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRacingCompany.getGasPrice()).isEqualTo(DEFAULT_GAS_PRICE);
        assertThat(testRacingCompany.getServicePrice()).isEqualTo(DEFAULT_SERVICE_PRICE);
        assertThat(testRacingCompany.getStreet()).isEqualTo(DEFAULT_STREET);
        assertThat(testRacingCompany.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testRacingCompany.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testRacingCompany.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);
        assertThat(testRacingCompany.getRacingCode()).isEqualTo(DEFAULT_RACING_CODE);

        // Validate the RacingCompany in Elasticsearch
        verify(mockRacingCompanySearchRepository, times(1)).save(testRacingCompany);
    }

    @Test
    @Transactional
    public void createRacingCompanyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = racingCompanyRepository.findAll().size();

        // Create the RacingCompany with an existing ID
        racingCompany.setId(1L);
        RacingCompanyDTO racingCompanyDTO = racingCompanyMapper.toDto(racingCompany);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRacingCompanyMockMvc.perform(post("/api/racing-companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(racingCompanyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RacingCompany in the database
        List<RacingCompany> racingCompanyList = racingCompanyRepository.findAll();
        assertThat(racingCompanyList).hasSize(databaseSizeBeforeCreate);

        // Validate the RacingCompany in Elasticsearch
        verify(mockRacingCompanySearchRepository, times(0)).save(racingCompany);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = racingCompanyRepository.findAll().size();
        // set the field null
        racingCompany.setName(null);

        // Create the RacingCompany, which fails.
        RacingCompanyDTO racingCompanyDTO = racingCompanyMapper.toDto(racingCompany);

        restRacingCompanyMockMvc.perform(post("/api/racing-companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(racingCompanyDTO)))
            .andExpect(status().isBadRequest());

        List<RacingCompany> racingCompanyList = racingCompanyRepository.findAll();
        assertThat(racingCompanyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGasPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = racingCompanyRepository.findAll().size();
        // set the field null
        racingCompany.setGasPrice(null);

        // Create the RacingCompany, which fails.
        RacingCompanyDTO racingCompanyDTO = racingCompanyMapper.toDto(racingCompany);

        restRacingCompanyMockMvc.perform(post("/api/racing-companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(racingCompanyDTO)))
            .andExpect(status().isBadRequest());

        List<RacingCompany> racingCompanyList = racingCompanyRepository.findAll();
        assertThat(racingCompanyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkServicePriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = racingCompanyRepository.findAll().size();
        // set the field null
        racingCompany.setServicePrice(null);

        // Create the RacingCompany, which fails.
        RacingCompanyDTO racingCompanyDTO = racingCompanyMapper.toDto(racingCompany);

        restRacingCompanyMockMvc.perform(post("/api/racing-companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(racingCompanyDTO)))
            .andExpect(status().isBadRequest());

        List<RacingCompany> racingCompanyList = racingCompanyRepository.findAll();
        assertThat(racingCompanyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStreetIsRequired() throws Exception {
        int databaseSizeBeforeTest = racingCompanyRepository.findAll().size();
        // set the field null
        racingCompany.setStreet(null);

        // Create the RacingCompany, which fails.
        RacingCompanyDTO racingCompanyDTO = racingCompanyMapper.toDto(racingCompany);

        restRacingCompanyMockMvc.perform(post("/api/racing-companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(racingCompanyDTO)))
            .andExpect(status().isBadRequest());

        List<RacingCompany> racingCompanyList = racingCompanyRepository.findAll();
        assertThat(racingCompanyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = racingCompanyRepository.findAll().size();
        // set the field null
        racingCompany.setCity(null);

        // Create the RacingCompany, which fails.
        RacingCompanyDTO racingCompanyDTO = racingCompanyMapper.toDto(racingCompany);

        restRacingCompanyMockMvc.perform(post("/api/racing-companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(racingCompanyDTO)))
            .andExpect(status().isBadRequest());

        List<RacingCompany> racingCompanyList = racingCompanyRepository.findAll();
        assertThat(racingCompanyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = racingCompanyRepository.findAll().size();
        // set the field null
        racingCompany.setState(null);

        // Create the RacingCompany, which fails.
        RacingCompanyDTO racingCompanyDTO = racingCompanyMapper.toDto(racingCompany);

        restRacingCompanyMockMvc.perform(post("/api/racing-companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(racingCompanyDTO)))
            .andExpect(status().isBadRequest());

        List<RacingCompany> racingCompanyList = racingCompanyRepository.findAll();
        assertThat(racingCompanyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkZipCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = racingCompanyRepository.findAll().size();
        // set the field null
        racingCompany.setZipCode(null);

        // Create the RacingCompany, which fails.
        RacingCompanyDTO racingCompanyDTO = racingCompanyMapper.toDto(racingCompany);

        restRacingCompanyMockMvc.perform(post("/api/racing-companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(racingCompanyDTO)))
            .andExpect(status().isBadRequest());

        List<RacingCompany> racingCompanyList = racingCompanyRepository.findAll();
        assertThat(racingCompanyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRacingCompanies() throws Exception {
        // Initialize the database
        racingCompanyRepository.saveAndFlush(racingCompany);

        // Get all the racingCompanyList
        restRacingCompanyMockMvc.perform(get("/api/racing-companies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(racingCompany.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].gasPrice").value(hasItem(DEFAULT_GAS_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].servicePrice").value(hasItem(DEFAULT_SERVICE_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE.toString())))
            .andExpect(jsonPath("$.[*].racingCode").value(hasItem(DEFAULT_RACING_CODE.toString())));
    }
    
    @Test
    @Transactional
    public void getRacingCompany() throws Exception {
        // Initialize the database
        racingCompanyRepository.saveAndFlush(racingCompany);

        // Get the racingCompany
        restRacingCompanyMockMvc.perform(get("/api/racing-companies/{id}", racingCompany.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(racingCompany.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.gasPrice").value(DEFAULT_GAS_PRICE.doubleValue()))
            .andExpect(jsonPath("$.servicePrice").value(DEFAULT_SERVICE_PRICE.doubleValue()))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE.toString()))
            .andExpect(jsonPath("$.racingCode").value(DEFAULT_RACING_CODE.toString()));
    }

    @Test
    @Transactional
    public void getAllRacingCompaniesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        racingCompanyRepository.saveAndFlush(racingCompany);

        // Get all the racingCompanyList where name equals to DEFAULT_NAME
        defaultRacingCompanyShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the racingCompanyList where name equals to UPDATED_NAME
        defaultRacingCompanyShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRacingCompaniesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        racingCompanyRepository.saveAndFlush(racingCompany);

        // Get all the racingCompanyList where name in DEFAULT_NAME or UPDATED_NAME
        defaultRacingCompanyShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the racingCompanyList where name equals to UPDATED_NAME
        defaultRacingCompanyShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRacingCompaniesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        racingCompanyRepository.saveAndFlush(racingCompany);

        // Get all the racingCompanyList where name is not null
        defaultRacingCompanyShouldBeFound("name.specified=true");

        // Get all the racingCompanyList where name is null
        defaultRacingCompanyShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllRacingCompaniesByGasPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        racingCompanyRepository.saveAndFlush(racingCompany);

        // Get all the racingCompanyList where gasPrice equals to DEFAULT_GAS_PRICE
        defaultRacingCompanyShouldBeFound("gasPrice.equals=" + DEFAULT_GAS_PRICE);

        // Get all the racingCompanyList where gasPrice equals to UPDATED_GAS_PRICE
        defaultRacingCompanyShouldNotBeFound("gasPrice.equals=" + UPDATED_GAS_PRICE);
    }

    @Test
    @Transactional
    public void getAllRacingCompaniesByGasPriceIsInShouldWork() throws Exception {
        // Initialize the database
        racingCompanyRepository.saveAndFlush(racingCompany);

        // Get all the racingCompanyList where gasPrice in DEFAULT_GAS_PRICE or UPDATED_GAS_PRICE
        defaultRacingCompanyShouldBeFound("gasPrice.in=" + DEFAULT_GAS_PRICE + "," + UPDATED_GAS_PRICE);

        // Get all the racingCompanyList where gasPrice equals to UPDATED_GAS_PRICE
        defaultRacingCompanyShouldNotBeFound("gasPrice.in=" + UPDATED_GAS_PRICE);
    }

    @Test
    @Transactional
    public void getAllRacingCompaniesByGasPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        racingCompanyRepository.saveAndFlush(racingCompany);

        // Get all the racingCompanyList where gasPrice is not null
        defaultRacingCompanyShouldBeFound("gasPrice.specified=true");

        // Get all the racingCompanyList where gasPrice is null
        defaultRacingCompanyShouldNotBeFound("gasPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllRacingCompaniesByServicePriceIsEqualToSomething() throws Exception {
        // Initialize the database
        racingCompanyRepository.saveAndFlush(racingCompany);

        // Get all the racingCompanyList where servicePrice equals to DEFAULT_SERVICE_PRICE
        defaultRacingCompanyShouldBeFound("servicePrice.equals=" + DEFAULT_SERVICE_PRICE);

        // Get all the racingCompanyList where servicePrice equals to UPDATED_SERVICE_PRICE
        defaultRacingCompanyShouldNotBeFound("servicePrice.equals=" + UPDATED_SERVICE_PRICE);
    }

    @Test
    @Transactional
    public void getAllRacingCompaniesByServicePriceIsInShouldWork() throws Exception {
        // Initialize the database
        racingCompanyRepository.saveAndFlush(racingCompany);

        // Get all the racingCompanyList where servicePrice in DEFAULT_SERVICE_PRICE or UPDATED_SERVICE_PRICE
        defaultRacingCompanyShouldBeFound("servicePrice.in=" + DEFAULT_SERVICE_PRICE + "," + UPDATED_SERVICE_PRICE);

        // Get all the racingCompanyList where servicePrice equals to UPDATED_SERVICE_PRICE
        defaultRacingCompanyShouldNotBeFound("servicePrice.in=" + UPDATED_SERVICE_PRICE);
    }

    @Test
    @Transactional
    public void getAllRacingCompaniesByServicePriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        racingCompanyRepository.saveAndFlush(racingCompany);

        // Get all the racingCompanyList where servicePrice is not null
        defaultRacingCompanyShouldBeFound("servicePrice.specified=true");

        // Get all the racingCompanyList where servicePrice is null
        defaultRacingCompanyShouldNotBeFound("servicePrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllRacingCompaniesByStreetIsEqualToSomething() throws Exception {
        // Initialize the database
        racingCompanyRepository.saveAndFlush(racingCompany);

        // Get all the racingCompanyList where street equals to DEFAULT_STREET
        defaultRacingCompanyShouldBeFound("street.equals=" + DEFAULT_STREET);

        // Get all the racingCompanyList where street equals to UPDATED_STREET
        defaultRacingCompanyShouldNotBeFound("street.equals=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    public void getAllRacingCompaniesByStreetIsInShouldWork() throws Exception {
        // Initialize the database
        racingCompanyRepository.saveAndFlush(racingCompany);

        // Get all the racingCompanyList where street in DEFAULT_STREET or UPDATED_STREET
        defaultRacingCompanyShouldBeFound("street.in=" + DEFAULT_STREET + "," + UPDATED_STREET);

        // Get all the racingCompanyList where street equals to UPDATED_STREET
        defaultRacingCompanyShouldNotBeFound("street.in=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    public void getAllRacingCompaniesByStreetIsNullOrNotNull() throws Exception {
        // Initialize the database
        racingCompanyRepository.saveAndFlush(racingCompany);

        // Get all the racingCompanyList where street is not null
        defaultRacingCompanyShouldBeFound("street.specified=true");

        // Get all the racingCompanyList where street is null
        defaultRacingCompanyShouldNotBeFound("street.specified=false");
    }

    @Test
    @Transactional
    public void getAllRacingCompaniesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        racingCompanyRepository.saveAndFlush(racingCompany);

        // Get all the racingCompanyList where city equals to DEFAULT_CITY
        defaultRacingCompanyShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the racingCompanyList where city equals to UPDATED_CITY
        defaultRacingCompanyShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllRacingCompaniesByCityIsInShouldWork() throws Exception {
        // Initialize the database
        racingCompanyRepository.saveAndFlush(racingCompany);

        // Get all the racingCompanyList where city in DEFAULT_CITY or UPDATED_CITY
        defaultRacingCompanyShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the racingCompanyList where city equals to UPDATED_CITY
        defaultRacingCompanyShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllRacingCompaniesByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        racingCompanyRepository.saveAndFlush(racingCompany);

        // Get all the racingCompanyList where city is not null
        defaultRacingCompanyShouldBeFound("city.specified=true");

        // Get all the racingCompanyList where city is null
        defaultRacingCompanyShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    public void getAllRacingCompaniesByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        racingCompanyRepository.saveAndFlush(racingCompany);

        // Get all the racingCompanyList where state equals to DEFAULT_STATE
        defaultRacingCompanyShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the racingCompanyList where state equals to UPDATED_STATE
        defaultRacingCompanyShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllRacingCompaniesByStateIsInShouldWork() throws Exception {
        // Initialize the database
        racingCompanyRepository.saveAndFlush(racingCompany);

        // Get all the racingCompanyList where state in DEFAULT_STATE or UPDATED_STATE
        defaultRacingCompanyShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the racingCompanyList where state equals to UPDATED_STATE
        defaultRacingCompanyShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllRacingCompaniesByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        racingCompanyRepository.saveAndFlush(racingCompany);

        // Get all the racingCompanyList where state is not null
        defaultRacingCompanyShouldBeFound("state.specified=true");

        // Get all the racingCompanyList where state is null
        defaultRacingCompanyShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    public void getAllRacingCompaniesByZipCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        racingCompanyRepository.saveAndFlush(racingCompany);

        // Get all the racingCompanyList where zipCode equals to DEFAULT_ZIP_CODE
        defaultRacingCompanyShouldBeFound("zipCode.equals=" + DEFAULT_ZIP_CODE);

        // Get all the racingCompanyList where zipCode equals to UPDATED_ZIP_CODE
        defaultRacingCompanyShouldNotBeFound("zipCode.equals=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    public void getAllRacingCompaniesByZipCodeIsInShouldWork() throws Exception {
        // Initialize the database
        racingCompanyRepository.saveAndFlush(racingCompany);

        // Get all the racingCompanyList where zipCode in DEFAULT_ZIP_CODE or UPDATED_ZIP_CODE
        defaultRacingCompanyShouldBeFound("zipCode.in=" + DEFAULT_ZIP_CODE + "," + UPDATED_ZIP_CODE);

        // Get all the racingCompanyList where zipCode equals to UPDATED_ZIP_CODE
        defaultRacingCompanyShouldNotBeFound("zipCode.in=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    public void getAllRacingCompaniesByZipCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        racingCompanyRepository.saveAndFlush(racingCompany);

        // Get all the racingCompanyList where zipCode is not null
        defaultRacingCompanyShouldBeFound("zipCode.specified=true");

        // Get all the racingCompanyList where zipCode is null
        defaultRacingCompanyShouldNotBeFound("zipCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllRacingCompaniesByRacingCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        racingCompanyRepository.saveAndFlush(racingCompany);

        // Get all the racingCompanyList where racingCode equals to DEFAULT_RACING_CODE
        defaultRacingCompanyShouldBeFound("racingCode.equals=" + DEFAULT_RACING_CODE);

        // Get all the racingCompanyList where racingCode equals to UPDATED_RACING_CODE
        defaultRacingCompanyShouldNotBeFound("racingCode.equals=" + UPDATED_RACING_CODE);
    }

    @Test
    @Transactional
    public void getAllRacingCompaniesByRacingCodeIsInShouldWork() throws Exception {
        // Initialize the database
        racingCompanyRepository.saveAndFlush(racingCompany);

        // Get all the racingCompanyList where racingCode in DEFAULT_RACING_CODE or UPDATED_RACING_CODE
        defaultRacingCompanyShouldBeFound("racingCode.in=" + DEFAULT_RACING_CODE + "," + UPDATED_RACING_CODE);

        // Get all the racingCompanyList where racingCode equals to UPDATED_RACING_CODE
        defaultRacingCompanyShouldNotBeFound("racingCode.in=" + UPDATED_RACING_CODE);
    }

    @Test
    @Transactional
    public void getAllRacingCompaniesByRacingCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        racingCompanyRepository.saveAndFlush(racingCompany);

        // Get all the racingCompanyList where racingCode is not null
        defaultRacingCompanyShouldBeFound("racingCode.specified=true");

        // Get all the racingCompanyList where racingCode is null
        defaultRacingCompanyShouldNotBeFound("racingCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllRacingCompaniesByOwnerIsEqualToSomething() throws Exception {
        // Get already existing entity
        User owner = racingCompany.getOwner();
        racingCompanyRepository.saveAndFlush(racingCompany);
        Long ownerId = owner.getId();

        // Get all the racingCompanyList where owner equals to ownerId
        defaultRacingCompanyShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the racingCompanyList where owner equals to ownerId + 1
        defaultRacingCompanyShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRacingCompanyShouldBeFound(String filter) throws Exception {
        restRacingCompanyMockMvc.perform(get("/api/racing-companies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(racingCompany.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].gasPrice").value(hasItem(DEFAULT_GAS_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].servicePrice").value(hasItem(DEFAULT_SERVICE_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].racingCode").value(hasItem(DEFAULT_RACING_CODE)));

        // Check, that the count call also returns 1
        restRacingCompanyMockMvc.perform(get("/api/racing-companies/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRacingCompanyShouldNotBeFound(String filter) throws Exception {
        restRacingCompanyMockMvc.perform(get("/api/racing-companies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRacingCompanyMockMvc.perform(get("/api/racing-companies/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingRacingCompany() throws Exception {
        // Get the racingCompany
        restRacingCompanyMockMvc.perform(get("/api/racing-companies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRacingCompany() throws Exception {
        // Initialize the database
        racingCompanyRepository.saveAndFlush(racingCompany);

        int databaseSizeBeforeUpdate = racingCompanyRepository.findAll().size();

        // Update the racingCompany
        RacingCompany updatedRacingCompany = racingCompanyRepository.findById(racingCompany.getId()).get();
        // Disconnect from session so that the updates on updatedRacingCompany are not directly saved in db
        em.detach(updatedRacingCompany);
        updatedRacingCompany
            .name(UPDATED_NAME)
            .gasPrice(UPDATED_GAS_PRICE)
            .servicePrice(UPDATED_SERVICE_PRICE)
            .street(UPDATED_STREET)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .racingCode(UPDATED_RACING_CODE);
        RacingCompanyDTO racingCompanyDTO = racingCompanyMapper.toDto(updatedRacingCompany);

        restRacingCompanyMockMvc.perform(put("/api/racing-companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(racingCompanyDTO)))
            .andExpect(status().isOk());

        // Validate the RacingCompany in the database
        List<RacingCompany> racingCompanyList = racingCompanyRepository.findAll();
        assertThat(racingCompanyList).hasSize(databaseSizeBeforeUpdate);
        RacingCompany testRacingCompany = racingCompanyList.get(racingCompanyList.size() - 1);
        assertThat(testRacingCompany.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRacingCompany.getGasPrice()).isEqualTo(UPDATED_GAS_PRICE);
        assertThat(testRacingCompany.getServicePrice()).isEqualTo(UPDATED_SERVICE_PRICE);
        assertThat(testRacingCompany.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testRacingCompany.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testRacingCompany.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testRacingCompany.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testRacingCompany.getRacingCode()).isEqualTo(UPDATED_RACING_CODE);

        // Validate the RacingCompany in Elasticsearch
        verify(mockRacingCompanySearchRepository, times(1)).save(testRacingCompany);
    }

    @Test
    @Transactional
    public void updateNonExistingRacingCompany() throws Exception {
        int databaseSizeBeforeUpdate = racingCompanyRepository.findAll().size();

        // Create the RacingCompany
        RacingCompanyDTO racingCompanyDTO = racingCompanyMapper.toDto(racingCompany);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRacingCompanyMockMvc.perform(put("/api/racing-companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(racingCompanyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RacingCompany in the database
        List<RacingCompany> racingCompanyList = racingCompanyRepository.findAll();
        assertThat(racingCompanyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RacingCompany in Elasticsearch
        verify(mockRacingCompanySearchRepository, times(0)).save(racingCompany);
    }

    @Test
    @Transactional
    public void deleteRacingCompany() throws Exception {
        // Initialize the database
        racingCompanyRepository.saveAndFlush(racingCompany);

        int databaseSizeBeforeDelete = racingCompanyRepository.findAll().size();

        // Delete the racingCompany
        restRacingCompanyMockMvc.perform(delete("/api/racing-companies/{id}", racingCompany.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RacingCompany> racingCompanyList = racingCompanyRepository.findAll();
        assertThat(racingCompanyList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RacingCompany in Elasticsearch
        verify(mockRacingCompanySearchRepository, times(1)).deleteById(racingCompany.getId());
    }

    @Test
    @Transactional
    public void searchRacingCompany() throws Exception {
        // Initialize the database
        racingCompanyRepository.saveAndFlush(racingCompany);
        when(mockRacingCompanySearchRepository.search(queryStringQuery("id:" + racingCompany.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(racingCompany), PageRequest.of(0, 1), 1));
        // Search the racingCompany
        restRacingCompanyMockMvc.perform(get("/api/_search/racing-companies?query=id:" + racingCompany.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(racingCompany.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].gasPrice").value(hasItem(DEFAULT_GAS_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].servicePrice").value(hasItem(DEFAULT_SERVICE_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].racingCode").value(hasItem(DEFAULT_RACING_CODE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RacingCompany.class);
        RacingCompany racingCompany1 = new RacingCompany();
        racingCompany1.setId(1L);
        RacingCompany racingCompany2 = new RacingCompany();
        racingCompany2.setId(racingCompany1.getId());
        assertThat(racingCompany1).isEqualTo(racingCompany2);
        racingCompany2.setId(2L);
        assertThat(racingCompany1).isNotEqualTo(racingCompany2);
        racingCompany1.setId(null);
        assertThat(racingCompany1).isNotEqualTo(racingCompany2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RacingCompanyDTO.class);
        RacingCompanyDTO racingCompanyDTO1 = new RacingCompanyDTO();
        racingCompanyDTO1.setId(1L);
        RacingCompanyDTO racingCompanyDTO2 = new RacingCompanyDTO();
        assertThat(racingCompanyDTO1).isNotEqualTo(racingCompanyDTO2);
        racingCompanyDTO2.setId(racingCompanyDTO1.getId());
        assertThat(racingCompanyDTO1).isEqualTo(racingCompanyDTO2);
        racingCompanyDTO2.setId(2L);
        assertThat(racingCompanyDTO1).isNotEqualTo(racingCompanyDTO2);
        racingCompanyDTO1.setId(null);
        assertThat(racingCompanyDTO1).isNotEqualTo(racingCompanyDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(racingCompanyMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(racingCompanyMapper.fromId(null)).isNull();
    }
}
