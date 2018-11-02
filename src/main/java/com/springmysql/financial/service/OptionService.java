package com.springmysql.financial.service;

import com.springmysql.financial.model.Option;
import com.springmysql.financial.repository.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public class OptionService{

    @Autowired
    OptionRepository optionRepository;

    public void save(Option option) {
        optionRepository.save(option);
    }

    public List<Option> findAll() {
        return optionRepository.findAll();
    }

    public void deleteByOCC(String OCC) {
        optionRepository.deleteById(OCC);
    }
}
