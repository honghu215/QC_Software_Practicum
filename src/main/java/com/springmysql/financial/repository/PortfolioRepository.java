package com.springmysql.financial.repository;

import com.springmysql.financial.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    Portfolio findByUserNameAndStockName(String userName, String stockName);

    List<Portfolio> findAllByUserName(String userName);

    List<Portfolio> findAllByUserNameAndQuantityNot(String userName, int min);
}
