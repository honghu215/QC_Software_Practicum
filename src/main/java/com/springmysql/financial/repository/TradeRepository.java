package com.springmysql.financial.repository;

import com.springmysql.financial.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long> {

    List<Trade> findAllByUserNameOrderByDatetimeDesc(String userName);

    List<Trade> findAllByUserNameAndStockNameOrderByDatetimeDesc(String userName, String stockName);

}
