package com.springmysql.financial.repository;

import com.springmysql.financial.model.StockPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<StockPortfolio, Long> {

    StockPortfolio findByUserNameAndStockName(String userName, String stockName);

    List<StockPortfolio> findAllByUserName(String userName);

    List<StockPortfolio> findAllByUserNameAndQuantityNot(String userName, int min);
}
