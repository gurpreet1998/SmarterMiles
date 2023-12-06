package com.smartermiles.app.model;

import org.springframework.stereotype.Component;

@Component
public class RouteDataResponse {
    String routeDetails;

    public RouteDataResponse(String routeDetails) {
        this.routeDetails = routeDetails;
    }

    public RouteDataResponse() {
    }

    public String getRouteDetails() {
        return routeDetails;
    }

    public void setRouteDetails(String routeDetails) {
        this.routeDetails = routeDetails;
    }
}
