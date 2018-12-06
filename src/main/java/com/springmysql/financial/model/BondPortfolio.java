package com.springmysql.financial.model;

import javax.persistence.*;

@Entity
@Table(name = "bond_portfolio")
public class BondPortfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int bondPortfolioId;

    @Column(name = "username")
    private String userName;

    @Column(name = "bondName")
    private String bondName;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "asset")
    private double asset;

    public BondPortfolio() {
    }


    public BondPortfolio(String userName, String bondName, int quantity) {
        this.userName = userName;
        this.bondName = bondName;
        this.quantity = quantity;
        this.asset = 0.0;
    }

    public BondPortfolio(String userName, String bondName, int quantity, double asset) {
        this.userName = userName;
        this.bondName = bondName;
        this.quantity = quantity;
        this.asset = asset;
    }

    public double getAsset() {
        return asset;
    }

    public void setAsset(double asset) {
        this.asset = asset;
    }

    public int getBondPortfolioId() {
        return bondPortfolioId;
    }

    public String getUserName() {
        return userName;
    }

    public String getBondName() {
        return bondName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setBondName(String bondName) {
        this.bondName = bondName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "BondPortfolio{" +
                "bondPortfolioId=" + bondPortfolioId +
                ", userName='" + userName + '\'' +
                ", bondName='" + bondName + '\'' +
                ", quantity=" + quantity +
                ", asset=" + asset +
                '}';
    }
}
