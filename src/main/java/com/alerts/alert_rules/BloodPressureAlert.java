package com.alerts.alert_rules;

import com.alerts.Alert;
import com.datamanagement.Patient;
import com.datamanagement.PatientRecord;

import java.util.List;

public class BloodPressureAlert extends Alert implements AlertRule {
    public BloodPressureAlert(String patientId, String condition, long timestamp) {
        super(Integer.parseInt(patientId), condition, timestamp);
    }

    @Override
    public Alert evaluate(int patientID, List<PatientRecord> patientRecords) {
        String[] types = { "SystolicPressure", "DiastolicPressure" };

        for (String type : types) {
            List<PatientRecord> last3 = getLastRecords(patientRecords, type, 3);   // newest → oldest
            if (last3.isEmpty()) continue;                                       // no data to test

            double latest   = ((Number) last3.get(0).getMeasurementValue()).doubleValue();
            long   time     =  last3.get(0).getTimestamp();

            /* ---------- 1. critical thresholds ---------------------------- */
            if ("SystolicPressure".equals(type)) {
                if (latest > 180) return new BloodPressureAlert(
                        String.valueOf(patientID), "SYSTOLIC_HIGH", time);
                if (latest <  90) return new BloodPressureAlert(
                        String.valueOf(patientID), "SYSTOLIC_LOW",  time);
            } else { // Diastolic
                if (latest > 120) return new BloodPressureAlert(
                        String.valueOf(patientID), "DIASTOLIC_HIGH", time);
                if (latest <  60) return new BloodPressureAlert(
                        String.valueOf(patientID), "DIASTOLIC_LOW",  time);
            }

            /* ---------- 2. three‑point trend (+/‑ 10 mmHg each step) ------ */
            if (last3.size() == 3) {
                double v0 = ((Number) last3.get(0).getMeasurementValue()).doubleValue(); // newest
                double v1 = ((Number) last3.get(1).getMeasurementValue()).doubleValue();
                double v2 = ((Number) last3.get(2).getMeasurementValue()).doubleValue(); // oldest

                double d1 = v1 - v2;   // older → middle
                double d2 = v0 - v1;   // middle → newest

                if (d1 > 10 && d2 > 10) {                           // rising trend
                    return new BloodPressureAlert(
                            String.valueOf(patientID),
                            type.toUpperCase() + "_TREND_UP", time);
                }
                if (d1 < -10 && d2 < -10) {                         // falling trend
                    return new BloodPressureAlert(
                            String.valueOf(patientID),
                            type.toUpperCase() + "_TREND_DOWN", time);
                }
            }
        }
        /* No alert */
        return null;
    }

    public static void main(String[] args) {
        Patient patient = new Patient(1);
        patient.addRecord(100, "SystolicPressure", 1);
        patient.addRecord(111, "SystolicPressure", 2);
        patient.addRecord(122, "SystolicPressure", 3);
        Patient patient2 = new Patient(2);
        patient2.addRecord(100, "DiastolicPressure", 1);
        patient2.addRecord(111, "DiastolicPressure", 2);
        patient2.addRecord(122, "DiastolicPressure", 3);
    }
}

