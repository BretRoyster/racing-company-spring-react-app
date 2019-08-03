package us.rabtrade.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A RacingCompany.
 */
@Entity
@Table(name = "racing_company")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "racingcompany")
public class RacingCompany implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "gas_price", nullable = false)
    private Float gasPrice;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "service_price", nullable = false)
    private Float servicePrice;

    @NotNull
    @Column(name = "street", nullable = false)
    private String street;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @NotNull
    @Column(name = "state", nullable = false)
    private String state;

    @NotNull
    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    @Column(name = "racing_code")
    private String racingCode;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("racingCompanies")
    private User owner;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public RacingCompany name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getGasPrice() {
        return gasPrice;
    }

    public RacingCompany gasPrice(Float gasPrice) {
        this.gasPrice = gasPrice;
        return this;
    }

    public void setGasPrice(Float gasPrice) {
        this.gasPrice = gasPrice;
    }

    public Float getServicePrice() {
        return servicePrice;
    }

    public RacingCompany servicePrice(Float servicePrice) {
        this.servicePrice = servicePrice;
        return this;
    }

    public void setServicePrice(Float servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getStreet() {
        return street;
    }

    public RacingCompany street(String street) {
        this.street = street;
        return this;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public RacingCompany city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public RacingCompany state(String state) {
        this.state = state;
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public RacingCompany zipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getRacingCode() {
        return racingCode;
    }

    public RacingCompany racingCode(String racingCode) {
        this.racingCode = racingCode;
        return this;
    }

    public void setRacingCode(String racingCode) {
        this.racingCode = racingCode;
    }

    public User getOwner() {
        return owner;
    }

    public RacingCompany owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RacingCompany)) {
            return false;
        }
        return id != null && id.equals(((RacingCompany) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "RacingCompany{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", gasPrice=" + getGasPrice() +
            ", servicePrice=" + getServicePrice() +
            ", street='" + getStreet() + "'" +
            ", city='" + getCity() + "'" +
            ", state='" + getState() + "'" +
            ", zipCode='" + getZipCode() + "'" +
            ", racingCode='" + getRacingCode() + "'" +
            "}";
    }
}
