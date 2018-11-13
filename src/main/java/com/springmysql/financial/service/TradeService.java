package com.springmysql.financial.service;

import com.springmysql.financial.model.Trade;
import com.springmysql.financial.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public class TradeService {

    @Autowired
    TradeRepository tradeRepository;

    public List<Trade> findAll() {
        return tradeRepository.findAll();
    }

    public Trade save(Trade trade) {
        return tradeRepository.save(trade);
    }

    public List<Trade> findAllByUserName(String userName) {
        return tradeRepository.findAllByUserName(userName);
    }

    public List<Trade> findAllByUserNameAndStockName(String userName, String stockName) {
        return tradeRepository.findAllByUserNameAndStockName(userName, stockName);
    }
}