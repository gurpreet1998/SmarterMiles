package com.smartermiles.app.repository;

import com.smartermiles.app.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

    // Approximate radius of Earth in kilometers
    double EARTH_RADIUS = 6371.0;

    @Query("SELECT r FROM Ride r WHERE " +
            "(" + EARTH_RADIUS + " * acos(cos(radians(:riderStartLat)) * cos(radians(r.startLatitude)) *" +
            "cos(radians(r.startLongitude) - radians(:riderStartLong)) + sin(radians(:riderStartLat)) *" +
            "sin(radians(r.startLatitude)))) < r.radius AND " +
            "(" + EARTH_RADIUS + " * acos(cos(radians(:riderEndLat)) * cos(radians(r.endLatitude)) *" +
            "cos(radians(r.endLongitude) - radians(:riderEndLong)) + sin(radians(:riderEndLat)) *" +
            "sin(radians(r.endLatitude)))) < r.radius")
    List<Ride> findRidesWithinDriverRadius(double riderStartLat, double riderStartLong,
                                           double riderEndLat, double riderEndLong);
}
