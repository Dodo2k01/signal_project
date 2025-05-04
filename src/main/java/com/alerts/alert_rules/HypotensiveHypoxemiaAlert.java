package com.alerts.alert_rules;

import com.alerts.Alert;
import com.datamanagement.PatientRecord;

import java.util.Comparator;
import java.util.List;

public class HypotensiveHypoxemiaAlert extends Alert implements AlertRule{

    public HypotensiveHypoxemiaAlert(String patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }

    @Override
    public Alert evaluate(int patientID, List<PatientRecord> patientRecords) {
        // 1) most recent SystolicPressure
        PatientRecord sys = patientRecords.stream()
                .filter(r -> "SystolicPressure".equalsIgnoreCase(r.getRecordType()))
                .max(Comparator.comparingLong(PatientRecord::getTimestamp))
                .orElse(null);

        // 2) most recent Saturation
        PatientRecord sat = patientRecords.stream()
                .filter(r -> "Saturation".equalsIgnoreCase(r.getRecordType()))
                .max(Comparator.comparingLong(PatientRecord::getTimestamp))
                .orElse(null);

        if (sys == null || sat == null) return null;  // missing data

        double sysVal = ((Number)sys.getMeasurementValue()).doubleValue();
        double satVal = Double.parseDouble(
                sat.getMeasurementValue().toString().replace("%",""));

        // 3) combined condition
        if (sysVal < 90 && satVal < 92) {
            long ts = Math.max(sys.getTimestamp(), sat.getTimestamp());
            return new HypotensiveHypoxemiaAlert(
                    String.valueOf(patientID),
                    "HYPOTENSIVE_HYPOXEMIA",
                    ts
            );
        }

        return null;
    }

}
