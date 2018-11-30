package com.springmysql.financial.repository;

import com.springmysql.financial.model.OptionPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionPortfolioRepository extends JpaRepository<OptionPortfolio, Long> {

    List<OptionPortfolio> findAllByUserName(String username);

    OptionPortfolio findByUserNameAndOptionName(String username, String optionName);

}
