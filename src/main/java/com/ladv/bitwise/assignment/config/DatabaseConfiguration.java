package com.ladv.bitwise.assignment.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EntityScan(basePackages = {"com.ladv.bitwise.assignment.domain"})
@Configuration
@EnableJpaRepositories(basePackages = {"com.ladv.bitwise.assignment.repository"})
@EnableTransactionManagement
public class DatabaseConfiguration {
}
