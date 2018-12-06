package com.trafficmon;

import java.util.Date;
import java.util.Calendar;


public abstract class ZoneBoundaryCrossing {

    private final Vehicle vehicle;
    private final long time;


    public ZoneBoundaryCrossing(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.time = System.currentTimeMillis();
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public long timestamp() {
        return time;
    }
}
