package com.smartermiles.app.model;

import org.springframework.stereotype.Component;

@Component
public class Location {
    Double latitute, longitude;

    public Location(Double latitute, Double longitude) {
        this.latitute = latitute;
        this.longitude = longitude;
    }

    public Double getLatitute() {
        return latitute;
    }

    public void setLatitute(Double latitute) {
        this.latitute = latitute;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
