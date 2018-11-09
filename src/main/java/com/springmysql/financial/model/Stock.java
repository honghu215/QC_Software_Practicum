package com.springmysql.financial.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;


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

    @Column(name = "price")
    private Double price;

    @Column(name = "stock_field")
    @NotEmpty(message = "*Please provide the field of the stock")
    private String stockField;

    public Stock() {     }

    public Stock(@NotEmpty(message = "*Please provide the name of the stock") String stockName, Double price, @NotEmpty(message = "*Please provide the field of the stock") String stockField) {
        this.stockName = stockName;
        this.price = price;
        this.stockField = stockField;
    }

    public int getStockId() {
        return stockId;
    }

    public String getStockName() {
        return stockName;
    }

    public Double getPrice() {
        return price;
    }

    public String getStockField() {
        return stockField;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setStockField(String stockField) {
        this.stockField = stockField;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "stockId=" + stockId +
                ", stockName='" + stockName + '\'' +
                ", price=" + price +
                ", stockField='" + stockField + '\'' +
                '}';
    }
}