package com.smartermiles.app.controller;

import com.smartermiles.app.model.RouteDataResponse;
import com.smartermiles.app.model.RouteInfoRequest;
import com.smartermiles.app.service.RoutingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/routes")
public class RoutingController {

    @Autowired
    public RoutingService routingService;

    @PostMapping("/getRoutes")
    public RouteDataResponse getRoutes(@RequestBody RouteInfoRequest routeInfoRequest){
        try{
            return routingService.getRoutes(routeInfoRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
