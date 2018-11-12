package com.springmysql.financial.service;


import com.springmysql.financial.model.Stock;
import com.springmysql.financial.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public class StockService {

    @Autowired
    StockRepository stockRepository;

    public Stock findStockByStockName(String stockName) {
        return stockRepository.findStockByStockName(stockName);
    }

    public Stock saveStock(Stock stock){
        return stockRepository.save(stock);
    }

    public List<Stock> findAll() {
        return stockRepository.findAll();
    }

    public Stock findByStockId(int id) {
        return stockRepository.findByStockId(id);
    }

    public void deleteByStockId(int id) {
        stockRepository.deleteByStockId(id);
    }

}
