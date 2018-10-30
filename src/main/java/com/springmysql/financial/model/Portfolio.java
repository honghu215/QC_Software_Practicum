package com.springmysql.financial.model;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "portfolio")
public class Portfolio implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portfolio_id")
    private int portforlioId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "stock_id")
    private String stockName;

    @Column(name = "quantity")
    private int quantity;

    public Portfolio() {
    }

    public Portfolio(int userId, String stockName, int quantity) {
        this.userId = userId;
        this.stockName = stockName;
        this.quantity = quantity;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPortforlioId() {
        return portforlioId;
    }

    public int getUserId() {
        return userId;
    }

    public String getStockName() {
        return stockName;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Portfolio{" +
                "portforlioId=" + portforlioId +
                ", userId=" + userId +
                ", stockName=" + stockName +
                ", quantity=" + quantity +
                '}';
    }
}
