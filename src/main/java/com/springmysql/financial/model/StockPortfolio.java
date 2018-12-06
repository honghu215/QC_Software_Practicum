package com.springmysql.financial.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "stock_portfolio")
public class StockPortfolio implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "portfolio_id")
    private int portforlioId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "stock_id")
    private String stockName;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "asset")
    private double asset;

    public StockPortfolio() {
    }

    public StockPortfolio(String userName, String stockName, int quantity) {
        this.userName = userName;
        this.stockName = stockName;
        this.quantity = quantity;
        this.asset = 0.0;
    }

    public StockPortfolio(String userName, String stockName, int quantity, double asset) {
        this.userName = userName;
        this.stockName = stockName;
        this.quantity = quantity;
        this.asset = asset;
    }

    public double getAsset() {
        return asset;
    }

    public void setAsset(double asset) {
        this.asset = asset;
    }

    public int getPortforlioId() {
        return portforlioId;
    }

    public String getUserName() {
        return userName;
    }

    public String getStockName() {
        return stockName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "StockPortfolio{" +
                "portforlioId=" + portforlioId +
                ", userName='" + userName + '\'' +
                ", stockName='" + stockName + '\'' +
                ", quantity=" + quantity +
                ", asset=" + asset +
                '}';
    }
}
