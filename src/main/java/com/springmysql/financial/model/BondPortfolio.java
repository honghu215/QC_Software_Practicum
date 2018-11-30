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

    public BondPortfolio() {
    }

    public BondPortfolio(String userName, String bondName, int quantity) {
        this.userName = userName;
        this.bondName = bondName;
        this.quantity = quantity;
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
                '}';
    }
}
