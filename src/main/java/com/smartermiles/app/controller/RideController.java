package com.smartermiles.app.controller;

import com.smartermiles.app.dto.RideDTO;
import com.smartermiles.app.model.Ride;
import com.smartermiles.app.service.RideService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping
    public ResponseEntity<Ride> publishRide(@RequestBody RideDTO rideDTO) {
        Ride createdRide = rideService.publishNewRide(rideDTO);
        return ResponseEntity.ok(createdRide);
    }

    @GetMapping("/searchAvailableRides")
    public ResponseEntity<List<Ride>> searchAvailableRides(
            @RequestParam double startLatitude,
            @RequestParam double startLongitude,
            @RequestParam double endLatitude,
            @RequestParam double endLongitude) {

        // Call the updated findAvailableRides method with both start and end location parameters
        List<Ride> availableRides = rideService.findAvailableRides(startLatitude, startLongitude, endLatitude, endLongitude);
        return ResponseEntity.ok(availableRides);
    }
    @PostMapping("/{rideId}/join")
    public ResponseEntity<?> joinRide(@PathVariable Long rideId) {
        boolean rideJoined = rideService.joinRide(rideId);
        if (rideJoined) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("Ride could not be joined, no seats available or ride does not exist.");
        }
    }
}
