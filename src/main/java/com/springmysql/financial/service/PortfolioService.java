package com.springmysql.financial.service;


import com.springmysql.financial.model.Portfolio;
import com.springmysql.financial.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public class PortfolioService {

    @Autowired
    PortfolioRepository portfolioRepository;

    public Portfolio findByUserNameAndStockName(String userName, String stockName) {
        return portfolioRepository.findByUserNameAndStockName(userName, stockName);
    }

    public void save(Portfolio portfolio) {
        portfolioRepository.save(portfolio);
    }

    public List<Portfolio> findAllByUserName(String userName) {
        return portfolioRepository.findAllByUserName(userName);
    }

    public List<Portfolio> findAllByUserNameAndQUantityNot(String userName, int min){
        return portfolioRepository.findAllByUserNameAndQuantityNot(userName, min);
    }


}