package us.rabtrade.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link us.rabtrade.domain.RacingCompany} entity.
 */
public class RacingCompanyDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    @DecimalMin(value = "0")
    private Float gasPrice;

    @NotNull
    @DecimalMin(value = "0")
    private Float servicePrice;

    @NotNull
    private String street;

    @NotNull
    private String city;

    @NotNull
    private String state;

    @NotNull
    private String zipCode;

    private String racingCode;


    private Long ownerId;

    private String ownerLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(Float gasPrice) {
        this.gasPrice = gasPrice;
    }

    public Float getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(Float servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getRacingCode() {
        return racingCode;
    }

    public void setRacingCode(String racingCode) {
        this.racingCode = racingCode;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long userId) {
        this.ownerId = userId;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String userLogin) {
        this.ownerLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RacingCompanyDTO racingCompanyDTO = (RacingCompanyDTO) o;
        if (racingCompanyDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), racingCompanyDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RacingCompanyDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", gasPrice=" + getGasPrice() +
            ", servicePrice=" + getServicePrice() +
            ", street='" + getStreet() + "'" +
            ", city='" + getCity() + "'" +
            ", state='" + getState() + "'" +
            ", zipCode='" + getZipCode() + "'" +
            ", racingCode='" + getRacingCode() + "'" +
            ", owner=" + getOwnerId() +
            ", owner='" + getOwnerLogin() + "'" +
            "}";
    }
}
