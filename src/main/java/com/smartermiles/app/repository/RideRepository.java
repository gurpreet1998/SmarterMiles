package com.smartermiles.app.repository;

import com.smartermiles.app.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
    // Custom query methods can be defined here if needed.
}
