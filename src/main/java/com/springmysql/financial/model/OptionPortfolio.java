package com.springmysql.financial.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "option_portfolio")
public class OptionPortfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int optionPortfolioId;

    @Column(name = "username")
    private String userName;

    @Column(name = "option_name")
    private String optionName;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "asset")
    private double asset;

    public OptionPortfolio() {
    }

    public OptionPortfolio(String userName, String optionName, int quantity) {
        this.userName = userName;
        this.optionName = optionName;
        this.quantity = quantity;
        this.asset = 0.0;
    }

    public OptionPortfolio(String userName, String optionName, int quantity, double asset) {
        this.userName = userName;
        this.optionName = optionName;
        this.quantity = quantity;
        this.asset = asset;
    }

    public double getAsset() {
        return asset;
    }

    public void setAsset(double asset) {
        this.asset = asset;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getOptionPortfolioId() {
        return optionPortfolioId;
    }

    public String getUserName() {
        return userName;
    }

    public String getOptionName() {
        return optionName;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "OptionPortfolio{" +
                "optionPortfolioId=" + optionPortfolioId +
                ", userName='" + userName + '\'' +
                ", optionName='" + optionName + '\'' +
                ", quantity=" + quantity +
                ", asset=" + asset +
                '}';
    }
}
