package com.springmysql.financial.model;


import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "trade")
public class Trade implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trade_id")
    private int id;

    @Column(name = "USER_NAME", nullable = false)
    private String userName;

    @Column(name = "STOCK_NAME", nullable = false)
    private String stockName;

    @Column(name = "STOCK_PRICE", nullable = false)
    private double stockPrice;

    @Column(name = "datetime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date datetime;

    @Column(name = "quantity", nullable = false)
    private int quantity;


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public void setStockPrice(double stockPrice) {
        this.stockPrice = stockPrice;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getStockName() {
        return stockName;
    }

    public double getStockPrice() {
        return stockPrice;
    }

    public Date getDatetime() {
        return datetime;
    }

    public int getQuantity() {
        return quantity;
    }

    public Trade() { }

    public Trade(String userName, String stockName, double stockPrice, Date datetime, int quantity) {
        this.userName = userName;
        this.stockName = stockName;
        this.stockPrice = stockPrice;
        this.datetime = datetime;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "id=" + id +
                ", userName=" + userName +
                ", stockName='" + stockName + '\'' +
                ", stockPrice=" + stockPrice +
                ", datetime=" + datetime +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trade trade = (Trade) o;
        return id == trade.id &&
                Double.compare(trade.stockPrice, stockPrice) == 0 &&
                quantity == trade.quantity &&
                Objects.equals(userName, trade.userName) &&
                Objects.equals(stockName, trade.stockName) &&
                Objects.equals(datetime, trade.datetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, stockName, stockPrice, datetime, quantity);
    }
}