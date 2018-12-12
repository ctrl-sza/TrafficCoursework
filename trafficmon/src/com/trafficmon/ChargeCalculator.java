package com.trafficmon;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ChargeCalculator {

        /* Using the new charging structure to calculate charges */
        private BigDecimal duration;
        private List<ZoneBoundaryCrossing> crossings;

        ChargeCalculator(BigDecimal duration, List<ZoneBoundaryCrossing> crossings) {
            this.duration = duration;
            this.crossings = crossings;
        }

        BigDecimal invoke() {
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
        private boolean isBefore2pm(List<ZoneBoundaryCrossing> crossings) {
            /* Compares a timestamp to check if its before 2pm */
            ZoneBoundaryCrossing firstCrossing = crossings.get(0);
            if (firstCrossing instanceof ExitEvent) {
                firstCrossing = crossings.get(1);
            }

            long firstEntryTime = firstCrossing.timestamp();

            String twoPM = "1400";

            int comparison = timeConverter(firstEntryTime).compareTo(twoPM);

            return comparison < 0;
        }

        private String timeConverter(long timestamp) {
            Date date = new Date(timestamp);
            DateFormat formatter = new SimpleDateFormat("HHmm");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

            return formatter.format(date);
        }
}
