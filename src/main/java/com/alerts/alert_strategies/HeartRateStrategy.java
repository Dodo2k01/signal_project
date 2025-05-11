package com.alerts.alert_strategies;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import com.alerts.alerts.Alert;
import com.alerts.alerts.ECGAlert;
import com.datamanagement.PatientRecord;
import com.Util;

public class HeartRateStrategy implements AlertStrategy{

    @Override
    public List<Alert> checkAlert(int patientId, List<PatientRecord> patientRecords) {
        List<Alert> alerts = new ArrayList<>();
        final int minRecords = 5;
        final long timeFrame = 60000;
        final double diffFromAvg = 0.25;
        for (int i = 0; i < differentTypes.length; i++) {
            String type = differentTypes[i];
            List<PatientRecord> records = getTypeRecords(patientRecords, type);
            if (records.isEmpty()) continue;
            PatientRecord firstRecord = records.get(0);
            int recordCount = records.size();
            Deque<PatientRecord> queue = new ArrayDeque<>();
            queue.add(firstRecord);
            long firstTimestamp = firstRecord.getTimestamp();
            long lastTimestamp;
            double mean = Util.parseDouble(firstRecord.getMeasurementValue().toString());
            double sum = mean;
            for (int j = 1; j < recordCount; j++) {
                double newValue = Util.parseDouble(records.get(j).getMeasurementValue().toString());
                lastTimestamp = records.get(j).getTimestamp();
                // Evict old samples
                while (!queue.isEmpty() && lastTimestamp - firstTimestamp > timeFrame) {
                    double value = Util.parseDouble(queue.removeFirst().getMeasurementValue().toString());
                    sum -= value;
                    mean = queue.isEmpty() ? 0 : sum / queue.size();
                    if (!queue.isEmpty()) {
                        firstTimestamp = queue.peekFirst().getTimestamp();
                    } else {
                        firstTimestamp = lastTimestamp;
                    }
                }

                if (queue.size() >= minRecords) {
                    if (mean + (mean * diffFromAvg) < newValue) {
                        alerts.add(new ECGAlert(
                                patientId,
                                "Unexpected Value Increase for " + type + " to " + newValue + " from the average of: " + mean,
                                lastTimestamp
                        ));
                    } else if (mean - (mean * diffFromAvg) > newValue) {
                        alerts.add(new ECGAlert(
                                patientId,
                                "Unexpected Value Decrease for " + type + " to " + newValue + " from the average of: " + mean,
                                lastTimestamp
                        ));
                    }
                }
                sum += newValue;
                queue.add(records.get(j));
                mean = sum / queue.size();
            }
        }
        return alerts;
    }
}
