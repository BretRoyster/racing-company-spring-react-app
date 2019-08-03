package us.rabtrade.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link us.rabtrade.domain.TruckStop} entity. This class is used
 * in {@link us.rabtrade.web.rest.TruckStopResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /truck-stops?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TruckStopCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private FloatFilter basePrice;

    private FloatFilter opisPrice;

    private StringFilter street;

    private StringFilter city;

    private StringFilter state;

    private StringFilter zipCode;

    private StringFilter racingCode;

    private LongFilter ownerId;

    public TruckStopCriteria(){
    }

    public TruckStopCriteria(TruckStopCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.basePrice = other.basePrice == null ? null : other.basePrice.copy();
        this.opisPrice = other.opisPrice == null ? null : other.opisPrice.copy();
        this.street = other.street == null ? null : other.street.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.zipCode = other.zipCode == null ? null : other.zipCode.copy();
        this.racingCode = other.racingCode == null ? null : other.racingCode.copy();
        this.ownerId = other.ownerId == null ? null : other.ownerId.copy();
    }

    @Override
    public TruckStopCriteria copy() {
        return new TruckStopCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public FloatFilter getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(FloatFilter basePrice) {
        this.basePrice = basePrice;
    }

    public FloatFilter getOpisPrice() {
        return opisPrice;
    }

    public void setOpisPrice(FloatFilter opisPrice) {
        this.opisPrice = opisPrice;
    }

    public StringFilter getStreet() {
        return street;
    }

    public void setStreet(StringFilter street) {
        this.street = street;
    }

    public StringFilter getCity() {
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getState() {
        return state;
    }

    public void setState(StringFilter state) {
        this.state = state;
    }

    public StringFilter getZipCode() {
        return zipCode;
    }

    public void setZipCode(StringFilter zipCode) {
        this.zipCode = zipCode;
    }

    public StringFilter getRacingCode() {
        return racingCode;
    }

    public void setRacingCode(StringFilter racingCode) {
        this.racingCode = racingCode;
    }

    public LongFilter getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(LongFilter ownerId) {
        this.ownerId = ownerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TruckStopCriteria that = (TruckStopCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(basePrice, that.basePrice) &&
            Objects.equals(opisPrice, that.opisPrice) &&
            Objects.equals(street, that.street) &&
            Objects.equals(city, that.city) &&
            Objects.equals(state, that.state) &&
            Objects.equals(zipCode, that.zipCode) &&
            Objects.equals(racingCode, that.racingCode) &&
            Objects.equals(ownerId, that.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        basePrice,
        opisPrice,
        street,
        city,
        state,
        zipCode,
        racingCode,
        ownerId
        );
    }

    @Override
    public String toString() {
        return "TruckStopCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (basePrice != null ? "basePrice=" + basePrice + ", " : "") +
                (opisPrice != null ? "opisPrice=" + opisPrice + ", " : "") +
                (street != null ? "street=" + street + ", " : "") +
                (city != null ? "city=" + city + ", " : "") +
                (state != null ? "state=" + state + ", " : "") +
                (zipCode != null ? "zipCode=" + zipCode + ", " : "") +
                (racingCode != null ? "racingCode=" + racingCode + ", " : "") +
                (ownerId != null ? "ownerId=" + ownerId + ", " : "") +
            "}";
    }

}
