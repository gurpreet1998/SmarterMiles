package com.smartermiles.app.service;

import com.smartermiles.app.dto.RideDTO;
import com.smartermiles.app.model.Ride;
import com.smartermiles.app.repository.RideRepository;
import com.smartermiles.app.service.RideService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;

    public RideServiceImpl(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    @Override
    public Ride publishNewRide(RideDTO rideDTO) {
        // Convert RideDTO to Ride entity
        Ride ride = new Ride();
        ride.setStartLatitude(rideDTO.getStartLatitude());
        ride.setStartLongitude(rideDTO.getStartLongitude());
        ride.setEndLatitude(rideDTO.getEndLatitude());
        ride.setEndLongitude(rideDTO.getEndLongitude());
        ride.setDepartureTime(rideDTO.getDepartureTime());
        ride.setVehicleType(rideDTO.getVehicleType());
        ride.setTotalSeats(rideDTO.getTotalSeats());
        ride.setAvailableSeats(rideDTO.getTotalSeats()); // Initially all seats are available
        ride.setRadius(rideDTO.getRadius());

        // Save the new ride to the database
        return rideRepository.save(ride);
    }

    @Override
    public List<Ride> findAvailableRides(double riderStartLat, double riderStartLong,
                                         double riderEndLat, double riderEndLong) {
        return rideRepository.findRidesWithinDriverRadius(riderStartLat, riderStartLong,
                riderEndLat, riderEndLong);
    }
    @Transactional
    @Override
    public boolean joinRide(Long rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new EntityNotFoundException("Ride not found"));

        if (ride.getAvailableSeats() > 0) {
            ride.setAvailableSeats(ride.getAvailableSeats() - 1);

            // Determine the increment based on the type of vehicle
            int greenScoreIncrement = "EV".equals(ride.getVehicleType()) ? 20 : 10;

            ride.setGreenScore(ride.getGreenScore() + greenScoreIncrement);
            rideRepository.save(ride);
            return true;
        } else {
            return false;
        }
    }

}
