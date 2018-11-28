package com.springmysql.financial.repository;

import com.springmysql.financial.model.BondTrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BondTradeRepository extends JpaRepository<BondTrade, Long> {

    List<BondTrade> findAllByUserNameOrderByDatetimeDesc(String username);

    BondTrade findByUserNameAndBondName(String username, String bondName);
}
