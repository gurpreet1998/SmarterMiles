package com.smartermiles.app.service;

import com.smartermiles.app.dto.RideDTO;
import com.smartermiles.app.model.Ride;
import java.util.List;

public interface RideService {
    Ride publishNewRide(RideDTO rideDTO);
    List<Ride> findAvailableRides(double riderStartLatitude, double riderStartLongitude,
                                  double riderEndLatitude, double riderEndLongitude);
}