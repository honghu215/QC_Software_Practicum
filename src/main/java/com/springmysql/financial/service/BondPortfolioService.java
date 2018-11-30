package com.springmysql.financial.service;

import com.springmysql.financial.model.BondPortfolio;
import com.springmysql.financial.repository.BondPortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public class BondPortfolioService {

    @Autowired
    BondPortfolioRepository bondPortfolioRepository;

    public List<BondPortfolio> findAllByUsername(String username) {
        return bondPortfolioRepository.findAllByUserName(username);
    }

    public BondPortfolio findByUsernameAndBondname(String username, String bondname) {
        return bondPortfolioRepository.findByUserNameAndBondName(username, bondname);
    }

    public void save(BondPortfolio bondPortfolio) {
        bondPortfolioRepository.save(bondPortfolio);
    }

    public List<BondPortfolio> findAllByUsernameAndQuantityNot(String username, int min){
        return bondPortfolioRepository.findAllByUserNameAndQuantityNot(username, min);
    }
}
