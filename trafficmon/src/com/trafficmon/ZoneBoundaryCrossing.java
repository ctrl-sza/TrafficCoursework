package com.trafficmon;

import java.util.Date;
import java.util.Calendar;


public abstract class ZoneBoundaryCrossing {

    private final Vehicle vehicle;
    private long time;


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

    public void setTime(long time) { this.time = time;}
}
