package com.trafficmon;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CongestionChargeSystemTest {

   @Test
    void calculateChargesTest() {
       CongestionChargeSystem ccs = new CongestionChargeSystem();

       Vehicle theFiat = Vehicle.withRegistration("A123 XYZ"); // using factory method (public)

       ccs.vehicleEnteringZone(theFiat);

       ccs.vehicleLeavingZone(theFiat);

       ccs.calculateCharges();
       // mock interfaces AccountService and Penalties Service to avoid tests failing cos of random amounts of credit in accounts
       // get string assertion that sysout is same as string we want

   }
}