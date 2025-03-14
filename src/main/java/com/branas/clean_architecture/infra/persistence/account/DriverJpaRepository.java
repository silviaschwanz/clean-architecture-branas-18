package com.branas.clean_architecture.infra.persistence.account;

import org.springframework.data.jpa.repository.JpaRepository;


public interface DriverJpaRepository extends JpaRepository<DriverEntity, Long> {
}
