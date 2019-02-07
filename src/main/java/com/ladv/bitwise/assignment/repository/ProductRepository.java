package com.ladv.bitwise.assignment.repository;

import com.ladv.bitwise.assignment.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findTop1ByIdIsNotNullOrderByIdDesc();
}
