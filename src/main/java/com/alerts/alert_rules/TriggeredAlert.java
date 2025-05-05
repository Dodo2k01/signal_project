package com.alerts.alert_rules;

import com.alerts.Alert;
import com.datamanagement.PatientRecord;

import java.util.Comparator;
import java.util.List;

public class TriggeredAlert extends Alert implements AlertRule{

    public TriggeredAlert(String patientId, String condition, long timestamp) {
        super(Integer.parseInt(patientId), condition, timestamp);
    }

    @Override
    public Alert evaluate(int patientID, List<PatientRecord> patientRecords) {
        // find the most recent “Alert” record (manual button events)
        return patientRecords.stream()
                .filter(r -> "Alert".equalsIgnoreCase(r.getRecordType()))
                .max(Comparator.comparingLong(PatientRecord::getTimestamp))
                .map(r -> {
                    String state = r.getMeasurementValue().toString().trim().toUpperCase();
                    // condition string is either “TRIGGERED” or “RESOLVED”
                    return new TriggeredAlert(
                            String.valueOf(patientID),
                            state,
                            r.getTimestamp()
                    );
                })
                .orElse(null);
    }

}
