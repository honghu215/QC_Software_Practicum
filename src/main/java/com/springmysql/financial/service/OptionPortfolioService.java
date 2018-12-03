package com.springmysql.financial.service;

import com.springmysql.financial.model.OptionPortfolio;
import com.springmysql.financial.repository.OptionPortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public class OptionPortfolioService {

    @Autowired
    OptionPortfolioRepository optionPortfolioRepository;

    public void save(OptionPortfolio optionPortfolio) {
        optionPortfolioRepository.save(optionPortfolio);
    }

    public List<OptionPortfolio> findAllByUsername(String username) {
        return optionPortfolioRepository.findAllByUserName(username);
    }

    public OptionPortfolio findByUserNameAndOptionName(String username, String optionName) {
        return optionPortfolioRepository.findByUserNameAndOptionName(username, optionName);
    }

    public void delete(OptionPortfolio optionPortfolio) {
        optionPortfolioRepository.delete(optionPortfolio);
    }
}
