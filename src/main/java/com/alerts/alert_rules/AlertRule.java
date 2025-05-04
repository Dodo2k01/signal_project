package com.alerts.alert_rules;

import com.alerts.Alert;
import com.datamanagement.PatientRecord;

import java.util.List;

public interface AlertRule {
public Alert evaluate(int patientID, List<PatientRecord> patientRecords);
}
