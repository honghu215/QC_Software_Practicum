package com.springmysql.financial.repository;

import com.springmysql.financial.model.Bond;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BondRepository extends JpaRepository<Bond, Long> {

    void deleteByBondId(int bondId);
}
