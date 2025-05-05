package com.alerts;

import com.datamanagement.PatientRecord;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// Represents an alert class
public class Alert {
    private int patientId;
    private String condition;
    private long timestamp;
    protected final String[] differentTypes = {"Alert","Cholesterol","DiastolicPressure","ECG","RedBloodCells","Saturation","SystolicPressure","WhiteBloodCells"};

    public Alert(int patientId, String condition, long timestamp) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
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

    /**
     * Fetches up to the last N records of the given type, newest first.
     */
    protected List<PatientRecord> getLastRecords(List<PatientRecord> recs,
                                                 String type,
                                                 int limit) {
        return recs.stream()
                .filter(r -> r.getRecordType().equalsIgnoreCase(type))
                .sorted(Comparator.comparingLong(PatientRecord::getTimestamp)
                        .reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

}
