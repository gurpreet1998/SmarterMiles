package com.smartermiles.app.repository;

import com.smartermiles.app.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    // Custom query methods can be defined here if needed
}
