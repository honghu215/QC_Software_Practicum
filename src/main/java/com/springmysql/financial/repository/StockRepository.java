package com.springmysql.financial.repository;

import com.springmysql.financial.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Stock findStockByStockName(String stockName);

    Stock findByStockId(int id);

    void deleteByStockId(int id);
}