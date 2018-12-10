package com.trafficmon;

import org.junit.jupiter.api.Test;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;


import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CongestionChargeSystemTest {

   @Rule
   //JUnitRuleMockery context = new JUnitRuleMockery();

    //AccountsService acctServ = context.mock(AccountsService.class);
    //Vehicle carA = Vehicle.withRegistration("LR59 PFK");
    //Account myAccount = new Account("Vikash Panjiyar", carA, new BigDecimal(1000000));



   @Test
    void calculateChargesTest() {
       CongestionChargeSystem ccs = new CongestionChargeSystem();

       Vehicle theFiat = Vehicle.withRegistration("A123 XYZ"); // using factory method (public)

       ccs.vehicleEnteringZone(theFiat);

       ccs.vehicleLeavingZone(theFiat);

       ccs.calculateAllCharges();
       // mock interfaces AccountService and Penalties Service to avoid tests failing cos of random amounts of credit in accounts
       // get string assertion that sysout is same as string we want


      //ccs.vehicleEnteringZone(carA);

      //ccs.vehicleLeavingZone(carA);

      //ccs.calculateAllCharges();
   }

   @Test
    void isAfter2pmTester () {
       CongestionChargeSystem ccs = new CongestionChargeSystem();
       Vehicle v = Vehicle.withRegistration("A123 XYZ");
       List<ZoneBoundaryCrossing> crossings = new ArrayList<ZoneBoundaryCrossing>();
       ccs.vehicleEnteringZone(v);
       long timestamp_one = crossings.get(0).timestamp();
       timestamp_one = 100000000;

       ccs.vehicleLeavingZone(v);
       long timestamp_two = crossings.get(1).timestamp();
       timestamp_two = 120000000;

       assertEquals(CongestionChargeSystem.isBefore2pm(crossings), false);

   }
           }