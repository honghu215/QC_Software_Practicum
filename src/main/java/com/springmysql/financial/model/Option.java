package com.springmysql.financial.model;


import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "`option`")
public class Option {

    @Id
    @Column(name = "occCode", nullable = false)
    private String occCode;

    @NotEmpty(message = "Please provide the option name.")
    @Column(name = "option_name")
    private String optionName;

    @Column(name = "strikePrice")
    @DecimalMin("0.01")
    private double strikePrice;

    @Column(name = "option_value")
    @DecimalMin("0.01")
    private double optionValue;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "createdOn")
    private Date createdOn;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "expiration")
    private Date expiration;

    @Column(name = "putCall")
    private String putCall;

    @Column(name = "ameEur")
    private String ameEur;

    @Column(name = "underlying")
    private String underlying;

    public Option() {
    }

    public Option(String occCode, String optionName, double strikePrice, double optionValue, Date createdOn, Date expiration, String putCall, String ameEur, String underlying) {
        this.optionName = optionName;
        this.strikePrice = strikePrice;
        this.expiration = expiration;
        this.createdOn = createdOn;
        this.putCall = putCall;
        this.ameEur = ameEur;
        this.underlying = underlying;
        this.optionValue = optionValue;

        Calendar cal0 = Calendar.getInstance();
        cal0.setTime(expiration);
        Calendar cal = Calendar.getInstance();
        cal.setTime(expiration);
        this.occCode = underlying.substring(0, 3).toUpperCase() + cal.get(Calendar.MONTH) + cal.get(Calendar.DAY_OF_MONTH) + cal.get(Calendar.YEAR)
                + putCall.toUpperCase().charAt(0) + strikePrice;
    }

    public double getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(double optionValue) {
        this.optionValue = optionValue;
    }

    public String getOccCode() {
        return occCode;
    }

    public String getOptionName() {
        return optionName;
    }

    public double getStrikePrice() {
        return strikePrice;
    }

    public Date getExpiration() {
        return expiration;
    }

    public String getPutCall() {
        return putCall;
    }

    public String getAmeEur() {
        return ameEur;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public String getUnderlying() {
        return underlying;
    }

    public void setOccCode(String occCode) {
        this.occCode = occCode;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public void setStrikePrice(double strikePrice) {
        this.strikePrice = strikePrice;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public void setPutCall(String putCall) {
        this.putCall = putCall;
    }

    public void setAmeEur(String ameEur) {
        this.ameEur = ameEur;
    }

    public void setUnderlying(String underlying) {
        this.underlying = underlying;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public String toString() {
        return "Option{" +
                "occCode='" + occCode + '\'' +
                ", optionName='" + optionName + '\'' +
                ", strikePrice=" + strikePrice +
                ", optionValue=" + optionValue +
                ", createdOn=" + createdOn +
                ", expiration=" + expiration +
                ", putCall='" + putCall + '\'' +
                ", ameEur='" + ameEur + '\'' +
                ", underlying='" + underlying + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Option option = (Option) o;
        return Double.compare(option.strikePrice, strikePrice) == 0 &&
                Double.compare(option.optionValue, optionValue) == 0 &&
                Objects.equals(occCode, option.occCode) &&
                Objects.equals(optionName, option.optionName) &&
                Objects.equals(createdOn, option.createdOn) &&
                Objects.equals(expiration, option.expiration) &&
                Objects.equals(putCall, option.putCall) &&
                Objects.equals(ameEur, option.ameEur) &&
                Objects.equals(underlying, option.underlying);
    }

    @Override
    public int hashCode() {
        return Objects.hash(occCode, optionName, strikePrice, optionValue, createdOn, expiration, putCall, ameEur, underlying);
    }
}
