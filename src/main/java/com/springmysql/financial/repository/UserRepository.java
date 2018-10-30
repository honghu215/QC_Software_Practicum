package com.springmysql.financial.repository;

import com.springmysql.financial.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    void deleteById(int id);
}
