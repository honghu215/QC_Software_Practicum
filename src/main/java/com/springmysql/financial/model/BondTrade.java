package com.springmysql.financial.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bond_trade")
public class BondTrade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "userName")
    private String userName;

    @Column(name = "bondName")
    private String bondName;

    @Column(name = "value")
    private double value;

    @Column(name = "coupon")
    private double coupon;

    @Column(name = "issuedOn")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date issuedOn;

    @Column(name = "maturityLength")
    private int maturityLength;

    @Column(name = "datetime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date datetime;

    @Column(name = "returnedTimes")
    private int returned = 0;
}
