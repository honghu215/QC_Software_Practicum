package com.springmysql.financial.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "bond")
public class Bond {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int bondId;

    @Column(name = "name")
    private String bondName;

    @Column(name = "maturityLength")
    private int maturityLength;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "maturity")
    private LocalDate maturity;

    @Column(name = "coupon")
    private double coupon;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "createdOn")
    private LocalDate createdOn;

    @Column(name = "bondValue")
    private Double bondValue;

    public Bond() {    }

    public Bond(String bondName, int maturityLength, double coupon) {
        this.bondName = bondName;
        this.maturityLength = maturityLength;
        this.coupon = coupon;
    }

    public Bond(String bondName, int maturityLength, LocalDate maturity, double coupon, LocalDate createdOn) {
        this.bondName = bondName;
        this.maturityLength = maturityLength;
        this.coupon = coupon;
        this.createdOn = createdOn;
    }

    public void setBondName(String bondName) {
        this.bondName = bondName;
    }

    public void setMaturityLength(int maturityLength) {
        this.maturityLength = maturityLength;
    }

    public void setMaturity(LocalDate maturity) {
        this.maturity = maturity;
    }

    public void setCoupon(double coupon) {
        this.coupon = coupon;
    }

    public void setBondValue(Double bondValue) {
        this.bondValue = bondValue;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public int getBondId() {
        return bondId;
    }

    public String getBondName() {
        return bondName;
    }

    public int getMaturityLength() {
        return maturityLength;
    }

    public LocalDate getMaturity() {
        return maturity;
    }

    public double getCoupon() {
        return coupon;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public Double getBondValue() {
        return bondValue;
    }

    @Override
    public String toString() {
        return "Bond{" +
                "bondId=" + bondId +
                ", bondName='" + bondName + '\'' +
                ", maturityLength=" + maturityLength +
                ", maturity=" + maturity +
                ", coupon=" + coupon +
                ", createdOn=" + createdOn +
                ", bondValue=" + bondValue +
                '}';
    }
}
