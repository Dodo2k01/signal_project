package com.alerts.alerts;

import com.datamanagement.Patient;
import com.datamanagement.PatientRecord;

import java.util.*;
import java.util.stream.Collectors;

import static com.datamanagement.Patient.getRecords;

public class Alert {
    protected int patientId;
    protected String condition;
    protected long timestamp;


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

}