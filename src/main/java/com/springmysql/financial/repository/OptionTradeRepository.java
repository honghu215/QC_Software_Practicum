package com.springmysql.financial.repository;

import com.springmysql.financial.model.OptionTrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionTradeRepository extends JpaRepository<OptionTrade, Long> {

    OptionTrade findByUserNameAndOptionName(String userName, String optionName);

    List<OptionTrade> findAllByUserNameOrderByDatetimeDesc(String username);

    List<OptionTrade> findDistinctOptionNameByUserName(String username);

}
