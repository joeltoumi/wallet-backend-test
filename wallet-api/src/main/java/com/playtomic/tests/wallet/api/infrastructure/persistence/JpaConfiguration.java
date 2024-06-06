package com.playtomic.tests.wallet.api.infrastructure.persistence;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
    basePackages = "com.playtomic.tests.wallet.api.infrastructure.persistence")
@EnableJpaAuditing
public class JpaConfiguration {}
