package com.springmysql.financial.service;

import com.springmysql.financial.model.BondTrade;
import com.springmysql.financial.repository.BondTradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public class BondTradeService {

    @Autowired
    BondTradeRepository bondTradeRepository;

    public List<BondTrade> findAllByUsernameOrderByDatetimeDesc(String username) {
        return bondTradeRepository.findAllByUserNameOrderByDatetimeDesc(username);
    }

    public void save(BondTrade bondTrade) {
        bondTradeRepository.save(bondTrade);
    }

    public BondTrade findByUsernameAndBondName(String username, String bondName) {
        return bondTradeRepository.findByUserNameAndBondName(username, bondName );
    }

}
