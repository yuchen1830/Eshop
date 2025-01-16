package com.eshop.repository;

import com.eshop.entity.EmailCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailCodeRepository extends JpaRepository<EmailCode, Long> {
    List<EmailCode> findByEmail(String email);
}
