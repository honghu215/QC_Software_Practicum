package com.springmysql.financial.repository;

import com.springmysql.financial.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, String> {

    Option findByOptionName(String optionName);
}
