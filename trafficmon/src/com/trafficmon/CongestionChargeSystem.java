package com.trafficmon;

import  java.math.BigDecimal;
import java.util.*;

public class CongestionChargeSystem {

    private final List<ZoneBoundaryCrossing> eventLog = new ArrayList<ZoneBoundaryCrossing>();
    /*
        every time vehicle crosses boundary, ZBC added to event log.
        ZBC contains reference to vehicle and time of that crossing
     */

    public void vehicleEnteringZone(Vehicle vehicle) {
        eventLog.add(new EntryEvent(vehicle));
    }

    public void vehicleLeavingZone(Vehicle vehicle) {
        if (!previouslyRegistered(vehicle)) {
            return;
        }
        eventLog.add(new ExitEvent(vehicle));
    }

    public void calculateCharges() {

        Map<Vehicle, List<ZoneBoundaryCrossing>> crossingsByVehicle = new HashMap<Vehicle, List<ZoneBoundaryCrossing>>();

        for (ZoneBoundaryCrossing crossing : eventLog) {
            if (!crossingsByVehicle.containsKey(crossing.getVehicle())) { // if vehicle not in crossingsByVehicle
                crossingsByVehicle.put(crossing.getVehicle(), new ArrayList<ZoneBoundaryCrossing>()); // add key (vehicle) and value (empty arraylist)
            }
            crossingsByVehicle.get(crossing.getVehicle()).add(crossing); // gets arraylist (value in hash map) and adds ZBC to it
        }

        for (Map.Entry<Vehicle, List<ZoneBoundaryCrossing>> vehicleCrossings : crossingsByVehicle.entrySet()) {
            Vehicle vehicle = vehicleCrossings.getKey();
            List<ZoneBoundaryCrossing> crossings = vehicleCrossings.getValue();

            if (!checkOrderingOf(crossings)) {
                OperationsTeam.getInstance().triggerInvestigationInto(vehicle); // if times of entry and exit not in time order trigger inspection
            } else {

                BigDecimal charge = calculateDurationInZone(crossings);

                try {
                    RegisteredCustomerAccountsService.getInstance().accountFor(vehicle).deduct(charge);
                } catch (InsufficientCreditException ice) {
                    OperationsTeam.getInstance().issuePenaltyNotice(vehicle, charge);
                } catch (AccountNotRegisteredException e) {
                    OperationsTeam.getInstance().issuePenaltyNotice(vehicle, charge);
                }
            }
        }
    }

    private BigDecimal calculateCharge(BigDecimal duration) {
        if ( duration > (240)
    }



    private BigDecimal calculateDurationInZone(List<ZoneBoundaryCrossing> crossings) {

        BigDecimal duration = new BigDecimal(0);

        ZoneBoundaryCrossing lastEvent = crossings.get(0); //same


        for (ZoneBoundaryCrossing crossing : crossings.subList(1, crossings.size())) {

            if (crossing instanceof ExitEvent) {
                duration = duration.add(
                        new BigDecimal(minutesBetween(lastEvent.timestamp(), crossing.timestamp())));
            }

            lastEvent = crossing; // same
        }

        return duration;
    }

    private boolean previouslyRegistered(Vehicle vehicle) {
        for (ZoneBoundaryCrossing crossing : eventLog) {
            if (crossing.getVehicle().equals(vehicle)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkOrderingOf(List<ZoneBoundaryCrossing> crossings) {

        ZoneBoundaryCrossing lastEvent = crossings.get(0);

        for (ZoneBoundaryCrossing crossing : crossings.subList(1, crossings.size())) {
            if (crossing.timestamp() < lastEvent.timestamp()) {
                return false;
            }
            if (crossing instanceof EntryEvent && lastEvent instanceof EntryEvent) {
                return false;
            }
            if (crossing instanceof ExitEvent && lastEvent instanceof ExitEvent) {
                return false;
            }
            lastEvent = crossing;
        }

        return true;
    }

    private int minutesBetween(long startTimeMs, long endTimeMs) {
        return (int) Math.ceil((endTimeMs - startTimeMs) / (1000.0 * 60.0));
    }

}
