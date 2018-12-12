package com.trafficmon;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;

class CongestionChargeSystemTest {

   @Rule
   public JUnitRuleMockery context = new JUnitRuleMockery();
   AccountsService acctServ = context.mock(AccountsService.class);
   CongestionChargeSystem mockCcs = new CongestionChargeSystem(acctServ);

    //Vehicle carA = Vehicle.withRegistration("LR59 PFK");
    //Account myAccount = new Account("Vikash Panjiyar", carA, new BigDecimal(1000000))
    CongestionChargeSystem ccs = new CongestionChargeSystem();
    Vehicle theFiat = Vehicle.withRegistration("A123 XYZ");
    Vehicle theVw = Vehicle.withRegistration("F773 3RE");


   @Test
    void calculateChargesTest() {
        // using factory method (public)

       ccs.vehicleEnteringZone(theFiat);

       ccs.vehicleLeavingZone(theFiat);

       ccs.calculateAllCharges();
       // mock interfaces AccountService and Penalties Service to avoid tests failing cos of random amounts of credit in accounts
       // get string assertion that sysout is same as string we want

   }

    @Test
    public void checkIfEventLogSizeIsCorrect() {
        ccs.vehicleEnteringZone(theFiat);
        ccs.vehicleLeavingZone(theFiat);
        Assert.assertEquals(ccs.getEventLog().size(), 2);

    }

    @Test
    public void calculateChargeForOneHourInMorning() {
       mockCcs.vehicleEnteringZone(theVw);
       mockCcs.vehicleLeavingZone(theVw);
       mockCcs.getEventLog().get(0).setTime(0); // 0000hrs
       mockCcs.getEventLog().get(1).setTime(3600000); // 0100hrs

       BigDecimal charge = mockCcs.calculateCharge(mockCcs.getDurationInZone(mockCcs.getEventLog()), mockCcs.getEventLog());

       Assert.assertThat(charge, is(new BigDecimal(6)));

    }

    @Test
    public void calculateChargeForOneHourInAfternoon() {
        mockCcs.vehicleEnteringZone(theVw);
        mockCcs.vehicleLeavingZone(theVw);
        mockCcs.getEventLog().get(0).setTime(54000000); // 1500hrs
        mockCcs.getEventLog().get(1).setTime(57600000); // 1600hrs

        BigDecimal charge = mockCcs.calculateCharge(mockCcs.getDurationInZone(mockCcs.getEventLog()), mockCcs.getEventLog());

        Assert.assertThat(charge, is(new BigDecimal(4)));

    }

    @Test
    public void calculateChargeForOverFourHoursInZone() {
        mockCcs.vehicleEnteringZone(theVw);
        mockCcs.vehicleLeavingZone(theVw);
        mockCcs.getEventLog().get(0).setTime(0); // 1500hrs
        mockCcs.getEventLog().get(1).setTime(57600000); // 1600hrs

        BigDecimal charge = mockCcs.calculateCharge(mockCcs.getDurationInZone(mockCcs.getEventLog()), mockCcs.getEventLog());

        Assert.assertThat(charge, is(new BigDecimal(12)));

    }


   /*@Test
    void isAfter2pmTester () {
       BigDecimal duration = new BigDecimal(0);
       List<ZoneBoundaryCrossing> crossings = [];

       CongestionChargeSystem.ChargeCalculator cc = new CongestionChargeSystem.ChargeCalculator(BigDecimal duration,
               List<ZoneBoundaryCrossing> crossings);



   }*/
}