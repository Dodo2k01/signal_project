package com.alerts.alert_rules;

import com.alerts.Alert;
import com.datamanagement.PatientRecord;

import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class ECGAlert extends Alert implements AlertRule {
    public ECGAlert(String patientId, String condition, long timestamp) {
        super(Integer.parseInt(patientId), condition, timestamp);
    }

    @Override
    public Alert evaluate(int patientID, List<PatientRecord> patientRecords) {
        // Example: flag if latest ECG > 2× its moving average over the last 10 readings
        List<PatientRecord> ecgs = patientRecords.stream()
                .filter(r -> "ECG".equalsIgnoreCase(r.getRecordType()))
                .sorted(Comparator.comparingLong(PatientRecord::getTimestamp))
                .toList();

        if (ecgs.size() < 2) return null;

        double avg = movingAverage(ecgs, 10);
        PatientRecord latest = ecgs.get(ecgs.size() - 1);
        double val = ((Number) latest.getMeasurementValue()).doubleValue();

        if (val > 2 * avg) {
            return new ECGAlert(
                    String.valueOf(patientID),
                    "ECG_PEAK",
                    latest.getTimestamp()
            );
        }
        return null;
    }

    /**
     * Compute the moving average over the last `windowSize` records in time‐order.
     * If there are fewer than windowSize records, averages all of them.
     */
    private double movingAverage(List<PatientRecord> records, int windowSize) {
        Deque<Double> window = new LinkedList<>();
        double sum = 0;

        for (PatientRecord r : records) {
            double v = ((Number) r.getMeasurementValue()).doubleValue();
            window.addLast(v);
            sum += v;
            if (window.size() > windowSize) {
                sum -= window.removeFirst();
            }
        }
        return sum / window.size();
    }
}
