package com.alerts;

import com.datamanagement.PatientRecord;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AlertRule {

    public abstract List<Alert> evaluate(int patientID, List<PatientRecord> patientRecords);

    protected List<PatientRecord> getTypeRecords(List<PatientRecord> recs,
                                                 String type) {
        return recs.stream()
                .filter(r -> r.getRecordType().equalsIgnoreCase(type))
                .sorted(Comparator.comparingLong(PatientRecord::getTimestamp))
                .collect(Collectors.toList());
    }

    protected Double parseDouble(String input) {
        try {
            // strip out any non-digit/decimal (e.g. “%”) first if you like:
            double measure = Double.parseDouble(input.replaceAll("[^0-9.–]", ""));
            return measure;
        } catch (NumberFormatException e) {
            return null;
        }
    }


}
