package com.alerts.alert_strategies;

import com.alerts.alerts.Alert;
import com.datamanagement.PatientRecord;

import java.util.ArrayList;
import java.util.List;


public class TriggeredAlertStrategy extends AlertStrategy {
    @Override
    public List<Alert> checkAlert(int patientID, List<PatientRecord> patientRecords) {
        List<Alert> alerts = new ArrayList<>();
        String type = "Alert";
        List<PatientRecord> records = getRecords(patientRecords, type);
        for (PatientRecord record : records) {
            if(record.getMeasurementValue().toString().equalsIgnoreCase(type))
                alerts.add(new Alert(patientID, "Alert " + record.getMeasurementValue().toString(), record.getTimestamp()));
        }
        return alerts;
    }
}
