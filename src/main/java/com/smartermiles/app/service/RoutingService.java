package com.smartermiles.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartermiles.app.model.Location;
import com.smartermiles.app.model.RouteDataResponse;
import com.smartermiles.app.model.RouteInfoRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoutingService {

    private final double EARTH_RADIUS = 6371.0;
    private final String INRIX_TOKEN_URL = "https://api.iq.inrix.com/auth/v1/appToken";
    private final String INRIX_ROUTE_API_URL = "https://api.iq.inrix.com/findRoute";
    private final RestTemplate restTemplate = new RestTemplate();

    public RouteDataResponse getRoutes(RouteInfoRequest routeInfoRequest){
        List<Location> riderSources = routeInfoRequest.getRiderSources();
        List<Location> riderDestinations = routeInfoRequest.getRiderDestinations();
        Location driverSource = routeInfoRequest.getMainSource();
        Location driverDestination = routeInfoRequest.getMainDestination();

        List<Location> orderOfLocations = orderLocations(driverSource, riderSources);
        Location lastSource = orderOfLocations.get(orderOfLocations.size()-1);
        Location firstDestination = findNearestNeighbor(lastSource, riderDestinations);
        riderDestinations.remove(firstDestination);
        orderOfLocations.addAll(orderLocations(firstDestination, riderDestinations));
        orderOfLocations.add(driverDestination);

        // get route details from inrix api
        RouteDataResponse routeDataResponse = new RouteDataResponse();
        routeDataResponse.setRouteDetails(routesFromInrix(orderOfLocations));
        return routeDataResponse;
    }

    private List<Location> orderLocations(Location firstLocation, List<Location> nextLocations){

        List<Location> unvisited = new ArrayList<>(nextLocations);
        List<Location> path = new ArrayList<>();
        Location currentLocation = firstLocation;
        path.add(currentLocation);

        while (!unvisited.isEmpty()) {
            Location nearestNeighbor = findNearestNeighbor(currentLocation, unvisited);
            path.add(nearestNeighbor);
            unvisited.remove(nearestNeighbor);
            currentLocation = nearestNeighbor;
        }

        return path;
    }

    private Location findNearestNeighbor(Location currentLocation, List<Location> unvisited) {
        double minDistance = Double.MAX_VALUE;
        Location nearestNeighbor = null;

        for (Location neighbor : unvisited) {
            double distance = calculateDistance(currentLocation, neighbor);
            if (distance < minDistance) {
                minDistance = distance;
                nearestNeighbor = neighbor;
            }
        }

        return nearestNeighbor;
    }

    private double calculateDistance(Location location1, Location location2) {
        double deltaLat = Math.toRadians(location2.getLatitute() - location1.getLatitute());
        double deltaLon = Math.toRadians(location2.getLongitude() - location1.getLongitude());

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(Math.toRadians(location1.getLatitute())) * Math.cos(Math.toRadians(location2.getLatitute())) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    private String routesFromInrix(List<Location> locations){
        String appId = "z6795yti13";
        String hashToken = "ejY3OTV5dGkxM3w3U1oxNWtBVnZsbGEyeGV6cEQzZDFMS1Bhd0ZmQ3NqOVNOcGZ5SkI1";
        String routeInfo = null;
        String authToken = null;
        try {
            authToken = getAuthToken(appId, hashToken);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (authToken != null) {
            routeInfo = getRouteInfo(authToken, locations);

            if (routeInfo != null) {
                System.out.println("INRIX Route Information: " + routeInfo);
            }
        }

        return routeInfo;
    }

    private String getAuthToken(String appId, String hashToken) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        String requestUrl = INRIX_TOKEN_URL + "?appId=" + appId + "&hashToken=" + hashToken;

        ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET,
                new HttpEntity<>(headers), String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            JsonNode tokenNode = jsonNode.path("result").path("token");

            return tokenNode.asText();
        } else {
            System.out.println("Error getting INRIX access token");
            return null;
        }
    }

    private String getRouteInfo(String authToken, List<Location> locations){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", "Bearer " + authToken);

        StringBuilder requestUrl = new StringBuilder(INRIX_ROUTE_API_URL + "?");
        for(int i=1; i<locations.size(); i++){
            if(i!=1){
                requestUrl.append("&");
            }
            requestUrl.append("wp_").append(i).append("=").append(locations.get(i).getLatitute()).append("%2C").append(locations.get(i).getLongitude());
        }
        requestUrl.append("&format=json");

        ResponseEntity<String> response = restTemplate.exchange(requestUrl.toString(), HttpMethod.GET,
                new HttpEntity<>(headers), String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            // Parse the JSON response to extract route information
            // Adapt this based on the actual structure of the INRIX API response
            return response.getBody();
        } else {
            System.out.println("Error getting INRIX route information");
            return null;
        }
    }
}
