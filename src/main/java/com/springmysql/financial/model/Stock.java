package com.springmysql.financial.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "stock_id")
    private int stockId;

    @Column(name = "stock_name")
    @NotEmpty(message = "*Please provide the name of the stock")
    private String stockName;


    @Column(name = "stock_field")
    @NotEmpty(message = "*Please provide the field of the stock")
    private String stockField;

}