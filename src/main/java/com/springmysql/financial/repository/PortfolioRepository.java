package com.springmysql.financial.repository;

import com.springmysql.financial.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    Portfolio findByUserNameAndStockName(String userName, String stockName);

}
