package com.springmysql.financial.model;


import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "`option`")
public class Option {

    @Id
    @Column(name = "occCode", nullable = false)
    private String occCode;

    @Column(name = "option_name")
    private String optionName;

    @Column(name = "strikePrice")
    private double strikePrice;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "expiration")
    private Date expiration;

    @Column(name = "putCall")
    private String putCall;

    @Column(name = "ameEur")
    private String ameEur;

    @Column(name = "underlying")
    private String underlying;

    public Option() { }

    public Option(String occCode, String optionName, double strikePrice, Date expiration, String putCall, String ameEur, String underlying) {

        this.optionName = optionName;
        this.strikePrice = strikePrice;
        this.expiration = expiration;
        this.putCall = putCall;
        this.ameEur = ameEur;
        this.underlying = underlying;

        Calendar cal = Calendar.getInstance();
        cal.setTime(expiration);
        this.occCode = underlying.substring(0, 3).toUpperCase() + cal.get(Calendar.MONTH) + cal.get(Calendar.DAY_OF_MONTH) + cal.get(Calendar.YEAR)
                        + putCall.toUpperCase().charAt(0) + strikePrice;
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

    @Override
    public String toString() {
        return "Option{" +
                "occCode='" + occCode + '\'' +
                ", optionName='" + optionName + '\'' +
                ", strikePrice=" + strikePrice +
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
                Objects.equals(occCode, option.occCode) &&
                Objects.equals(optionName, option.optionName) &&
                Objects.equals(expiration, option.expiration) &&
                Objects.equals(putCall, option.putCall) &&
                Objects.equals(ameEur, option.ameEur) &&
                Objects.equals(underlying, option.underlying);
    }

    @Override
    public int hashCode() {
        return Objects.hash(occCode, optionName, strikePrice, expiration, putCall, ameEur, underlying);
    }
}
