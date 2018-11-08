package com.springmysql.financial.repository;

import com.springmysql.financial.model.Index;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndexRepository extends JpaRepository<Index, Long> {

    void deleteByIndexId(int indexId);

}
