package us.rabtrade.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A TruckStop.
 */
@Entity
@Table(name = "truck_stop")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "truckstop")
public class TruckStop implements Serializable {

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
    @Column(name = "base_price", nullable = false)
    private Float basePrice;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "opis_price", nullable = false)
    private Float opisPrice;

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
    @JsonIgnoreProperties("truckStops")
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

    public TruckStop name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getBasePrice() {
        return basePrice;
    }

    public TruckStop basePrice(Float basePrice) {
        this.basePrice = basePrice;
        return this;
    }

    public void setBasePrice(Float basePrice) {
        this.basePrice = basePrice;
    }

    public Float getOpisPrice() {
        return opisPrice;
    }

    public TruckStop opisPrice(Float opisPrice) {
        this.opisPrice = opisPrice;
        return this;
    }

    public void setOpisPrice(Float opisPrice) {
        this.opisPrice = opisPrice;
    }

    public String getStreet() {
        return street;
    }

    public TruckStop street(String street) {
        this.street = street;
        return this;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public TruckStop city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public TruckStop state(String state) {
        this.state = state;
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public TruckStop zipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getRacingCode() {
        return racingCode;
    }

    public TruckStop racingCode(String racingCode) {
        this.racingCode = racingCode;
        return this;
    }

    public void setRacingCode(String racingCode) {
        this.racingCode = racingCode;
    }

    public User getOwner() {
        return owner;
    }

    public TruckStop owner(User user) {
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
        if (!(o instanceof TruckStop)) {
            return false;
        }
        return id != null && id.equals(((TruckStop) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TruckStop{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", basePrice=" + getBasePrice() +
            ", opisPrice=" + getOpisPrice() +
            ", street='" + getStreet() + "'" +
            ", city='" + getCity() + "'" +
            ", state='" + getState() + "'" +
            ", zipCode='" + getZipCode() + "'" +
            ", racingCode='" + getRacingCode() + "'" +
            "}";
    }
}
