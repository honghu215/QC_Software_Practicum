package com.springmysql.financial.repository;

import com.springmysql.financial.model.BondPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BondPortfolioRepository extends JpaRepository<BondPortfolio, Long> {

    List<BondPortfolio> findAllByUserName(String username);

    BondPortfolio findByUserNameAndBondName(String username, String bondname);

    List<BondPortfolio> findAllByUserNameAndQuantityNot(String username, int min);

}
