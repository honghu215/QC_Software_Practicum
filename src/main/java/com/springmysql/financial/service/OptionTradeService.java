package com.springmysql.financial.service;

import com.springmysql.financial.model.OptionTrade;
import com.springmysql.financial.repository.OptionTradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public class OptionTradeService {

    @Autowired
    OptionTradeRepository optionTradeRepository;

    public OptionTrade findByOptionName(String username, String optionName) {
        return optionTradeRepository.findByUserNameAndOptionName(username, optionName);
    }

    public List<OptionTrade> findAllByUsernameOrderByDatetimeDesc(String username) {
        return optionTradeRepository.findAllByUserNameOrderByDatetimeDesc(username);
    }

    public void save(OptionTrade newTrade) {
        optionTradeRepository.save(newTrade);
    }

    public List<OptionTrade> findDistinctOptionNameByUsername(String username) {
        return optionTradeRepository.findAllByUserNameOrderByDatetimeDesc(username);
    }
}
