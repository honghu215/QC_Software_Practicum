package com.springmysql.financial.service;


import com.springmysql.financial.model.StockPortfolio;
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

    public StockPortfolio findByUserNameAndStockName(String userName, String stockName) {
        return portfolioRepository.findByUserNameAndStockName(userName, stockName);
    }

    public void save(StockPortfolio stockPortfolio) {
        portfolioRepository.save(stockPortfolio);
    }

    public List<StockPortfolio> findAllByUserName(String userName) {
        return portfolioRepository.findAllByUserName(userName);
    }

    public List<StockPortfolio> findAllByUserNameAndQUantityNot(String userName, int min){
        return portfolioRepository.findAllByUserNameAndQuantityNot(userName, min);
    }


}