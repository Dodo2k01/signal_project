package com.alerts.alerts;

import com.datamanagement.Patient;
import com.datamanagement.PatientRecord;

import java.util.*;
import java.util.stream.Collectors;

public class Alert {
    protected int patientId;
    protected String condition;
    protected long timestamp;
    protected static final String[] differentTypes = {"Cholesterol", "DiastolicPressure", "RedBloodCells", "Saturation", "SystolicPressure", "WhiteBloodCells"};

    public Alert(int patientId, String condition, long timestamp) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
    }

    public String toString() {
        return "Alert [patientId=" + patientId + ", condition=" + condition + ", timestamp=" + timestamp + "]";
    }

    public int getPatientId() {
        return patientId;
    }

    public String getCondition() {
        return condition;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public static class TriggeredAlertRule {

        @Override
        public List<Alert> evaluate(int patientID, List<PatientRecord> patientRecords) {
            List<Alert> alerts = new ArrayList<>();
            String type = "Alert";
            List<PatientRecord> records = getTypeRecords(patientRecords, type);
            for (PatientRecord record : records) {
                if(record.getMeasurementValue().toString().equalsIgnoreCase(type))
                    alerts.add(new Alert(patientID, "Alert " + record.getMeasurementValue().toString(), record.getTimestamp()));
            }
            return alerts;
        }
    }
}