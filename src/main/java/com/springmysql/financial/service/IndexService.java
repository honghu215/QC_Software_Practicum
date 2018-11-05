package com.springmysql.financial.service;

import com.springmysql.financial.model.Index;
import com.springmysql.financial.repository.IndexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public class IndexService {

    @Autowired
    IndexRepository indexRepository;

    public void save(Index index) {
        indexRepository.save(index);
    }

    public List<Index> findAll() {
        return indexRepository.findAll();
    }

    public void deleteByIndexId(int id) {
        indexRepository.deleteByIndexId(id);
    }
}
