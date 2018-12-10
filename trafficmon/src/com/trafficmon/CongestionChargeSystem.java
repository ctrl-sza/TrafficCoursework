package com.trafficmon;

import  java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class CongestionChargeSystem {

    private final HashMap<Vehicle, List<ZoneBoundaryCrossing>> eventLog = new HashMap<>();
    /*
        every time vehicle crosses boundary, ZBC added to event log.
        ZBC contains reference to vehicle and time of that crossing
     */

    public void vehicleEnteringZone(Vehicle vehicle) {
        if(previouslyRegistered(vehicle)) {
            eventLog.put(vehicle, new ArrayList<>());
        }
        eventLog.get(vehicle).add(new EntryEvent(vehicle));
    }

    public void vehicleLeavingZone(Vehicle vehicle) {
        if (previouslyRegistered(vehicle)) {
            return;
        }
        eventLog.get(vehicle).add(new ExitEvent(vehicle));
    }

    public void calculateAllCharges() {

        for (Map.Entry<Vehicle, List<ZoneBoundaryCrossing>> vehicleCrossings : eventLog.entrySet()) {
            Vehicle vehicle = vehicleCrossings.getKey();
            List<ZoneBoundaryCrossing> crossings = vehicleCrossings.getValue();

            if (!checkOrderingOf(crossings)) {
                OperationsTeam.getInstance().triggerInvestigationInto(vehicle); // if times of entry and exit not in time order trigger inspection
            } else {

                BigDecimal charge = calculateCharge(calculateDurationInZone(crossings), crossings);

                try {
                    RegisteredCustomerAccountsService.getInstance().accountFor(vehicle).deduct(charge);
                } catch (InsufficientCreditException | AccountNotRegisteredException ice) {
                    OperationsTeam.getInstance().issuePenaltyNotice(vehicle, charge);
                }
            }
        }
    }

    private BigDecimal calculateCharge(BigDecimal duration, List<ZoneBoundaryCrossing> crossings) {

        return new ChargeCalculator(duration, crossings).invoke();

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
        return !eventLog.containsKey(vehicle);
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

    public static class ChargeCalculator {
        /* Using the new charging structure to calculate charges */
        private BigDecimal duration;
        private List<ZoneBoundaryCrossing> crossings;

        public ChargeCalculator(BigDecimal duration, List<ZoneBoundaryCrossing> crossings) {
            this.duration = duration;
            this.crossings = crossings;
        }

        public BigDecimal invoke() {
            BigDecimal fourHours = new BigDecimal(240.0);

            int comparison = duration.compareTo(fourHours);

            if (comparison > 0) {
                return new BigDecimal(12);
            } else if (isBefore2pm(crossings)) {
                return new BigDecimal(6);

            } else {
                return new BigDecimal(4);
            }
        }

        public boolean secondEntryChecker() {
            // Checking if the car left and came back within 4 hours
            // Returns true if it leaves and comes back, and false if not
            ZoneBoundaryCrossing firstEntry = crossings.get(0);
            if (firstEntry instanceof ExitEvent) {
                firstEntry = crossings.get(1);
            }

            try {
                ZoneBoundaryCrossing secondEntry = crossings.get(2);
                if (secondEntry instanceof ExitEvent) {
                    secondEntry = crossings.get(3);
                }

                long timeBetweenEntries = (minutesBetween(firstEntry.timestamp(),
                        secondEntry.timestamp()));
                return timeBetweenEntries > 240;
            } catch (NullPointerException npe) {
                return false;
            }
        }
    }

    private boolean isBefore2pm(List<ZoneBoundaryCrossing> crossings) {
        /* Compares a timestamp to check if its before 2pm */
        ZoneBoundaryCrossing firstCrossing = crossings.get(0);
        if (firstCrossing instanceof ExitEvent) {
            firstCrossing = crossings.get(1);
        }

        long firstEntryTime = firstCrossing.timestamp();

        String twoPM = "1400";

        int comparison = timeConverter(firstEntryTime).compareTo(twoPM);

        return comparison > 0;
    }

    private String timeConverter(long timestamp) {
        Date date = new Date(timestamp);
        DateFormat formatter = new SimpleDateFormat("HHmm");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        return formatter.format(date);
    }
}

