package com.springmysql.financial.service;

import com.springmysql.financial.model.Bond;
import com.springmysql.financial.repository.BondRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public class BondService {

    @Autowired
    BondRepository bondRepository;

    public void deleteByBondId(int bondId) {
        bondRepository.deleteByBondId(bondId);
    }

    public void save(Bond bond) {
        bondRepository.save(bond);
    }

    public List<Bond> findAll() {
        return bondRepository.findAll();
    }
}
