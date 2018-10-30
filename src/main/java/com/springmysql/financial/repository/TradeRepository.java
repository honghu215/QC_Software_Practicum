package com.springmysql.financial.repository;

import com.springmysql.financial.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long> {


}
