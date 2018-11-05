package com.springmysql.financial.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bond")
public class Bond {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int bondId;

    @Column(name = "name")
    private String bondName;

    @Column(name = "matuarity")
    private String matuarity;

    @Column(name = "coupon")
    private double coupon;
}
