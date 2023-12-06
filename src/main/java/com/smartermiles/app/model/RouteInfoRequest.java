package com.smartermiles.app.model;

import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class RouteInfoRequest {

    Location mainSource, mainDestination;
    List<Location> riderSources, riderDestinations;

    public RouteInfoRequest(Location mainSource, Location mainDestination, List<Location> riderSources, List<Location> riderDestinations) {
        this.mainSource = mainSource;
        this.mainDestination = mainDestination;
        this.riderSources = riderSources;
        this.riderDestinations = riderDestinations;
    }

    public Location getMainSource() {
        return mainSource;
    }

    public void setMainSource(Location mainSource) {
        this.mainSource = mainSource;
    }

    public Location getMainDestination() {
        return mainDestination;
    }

    public void setMainDestination(Location mainDestination) {
        this.mainDestination = mainDestination;
    }

    public List<Location> getRiderSources() {
        return riderSources;
    }

    public void setRiderSources(List<Location> riderSources) {
        this.riderSources = riderSources;
    }

    public List<Location> getRiderDestinations() {
        return riderDestinations;
    }

    public void setRiderDestinations(List<Location> riderDestinations) {
        this.riderDestinations = riderDestinations;
    }
}
