package com.springmysql.financial.model;


import lombok.NonNull;

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

    @Column(name = "USER_ID", nullable = false)
    private int userId;

    @Column(name = "STOCK_NAME", nullable = false)
    private String stockName;

    @Column(name = "STOCK_PRICE", nullable = false)
    private double stockPrice;

    @Column(name = "date", updatable = false, columnDefinition = "datetime default CURRENT_TIMESTAMP")
    private Date date;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public void setStockPrice(double stockPrice) {
        this.stockPrice = stockPrice;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getStockName() {
        return stockName;
    }

    public double getStockPrice() {
        return stockPrice;
    }

    public Date getDate() {
        return date;
    }

    public int getQuantity() {
        return quantity;
    }

    public Trade() { }

    public Trade(int userId, String stockName, String stockPrice, int quantity, Date date) {
        this.userId = userId;
        this.stockName = stockName;
        this.stockPrice = Double.parseDouble(stockPrice);
        this.quantity = quantity;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "id=" + id +
                ", userId=" + userId +
                ", stockName='" + stockName + '\'' +
                ", stockPrice=" + stockPrice +
                ", date=" + date +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trade trade = (Trade) o;
        return id == trade.id &&
                userId == trade.userId &&
                Double.compare(trade.stockPrice, stockPrice) == 0 &&
                quantity == trade.quantity &&
                Objects.equals(stockName, trade.stockName) &&
                Objects.equals(date, trade.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, stockName, stockPrice, date, quantity);
    }

}