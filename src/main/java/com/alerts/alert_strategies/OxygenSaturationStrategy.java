package com.alerts.alert_strategies;

import java.util.ArrayList;
import java.util.List;

import com.alerts.alerts.Alert;
import com.alerts.alerts.BloodOxygenAlert;
import com.datamanagement.PatientRecord;

public class OxygenSaturationStrategy implements AlertStrategy{

    private static final String saturation = "Saturation";

    @Override
    public List<Alert> checkAlert(int patientId, List<PatientRecord> patientRecords) {
        long min10 = 600000;
        List<Alert> alerts = new ArrayList<>();
        List<PatientRecord> saturationType = getTypeRecords(patientRecords, saturation);
        if (saturationType.isEmpty()) return alerts;
        for (int i = saturationType.size() - 1; i > -1; i--) { //increment from the last index bc the newest records are last
            PatientRecord patientRecord = saturationType.get(i);
            long tNow = patientRecord.getTimestamp();
            double saturationNow = Double.parseDouble(patientRecord.getMeasurementValue().toString().replace("%", ""));
            if (saturationNow < 92)
                alerts.add(new BloodOxygenAlert(patientId, "Saturation critically low: " + saturationNow, patientRecord.getTimestamp()));
            for (int j = i - 1; j >= 0; j--) {
                PatientRecord patientRecord1 = saturationType.get(j);
                long tPrev = patientRecord1.getTimestamp();
                double saturationPrev = Double.parseDouble(patientRecord1.getMeasurementValue().toString().replace("%", ""));
                if ((tNow - tPrev) > min10) {
                    continue;
                }
                if (saturationPrev * 0.95 > saturationNow) {
                    alerts.add(new BloodOxygenAlert(patientId, "Rapid Saturation drop from : " + saturationPrev + " to " + saturationNow, tNow));
                }
                if (j == 0 && saturationPrev < 92)
                    alerts.add(new BloodOxygenAlert(patientId, "Saturation critically low: " + saturationNow, patientRecord.getTimestamp()));
            }
        }

        List<PatientRecord> saturationType = getTypeRecords(patientRecords, "Saturation");
        List<PatientRecord> systolicType = getTypeRecords(patientRecords, "SystolicPressure");
        if (saturationType.isEmpty() || systolicType.isEmpty()) return alerts;
        for (int i = saturationType.size() - 1; i >= 0; i--) {
            PatientRecord patientRecordSaturation = saturationType.get(i);
            long tSaturation = patientRecordSaturation.getTimestamp();
            double saturationNow = Double.parseDouble(patientRecordSaturation.getMeasurementValue().toString().replace("%", ""));
            if (saturationNow > 92) continue;

            for (int j = systolicType.size() - 1; j >= 0; j--) {
                PatientRecord patientRecordSystolic = systolicType.get(j);
                long tSystolic = patientRecordSystolic.getTimestamp();
                if (Math.abs(tSystolic - tSaturation) > 600_000) continue;
                double systolicPressure = Double.parseDouble(systolicType.get(j).getMeasurementValue().toString());
                if (systolicPressure < 90)
                    alerts.add(new BloodOxygenAlert(patientId, "HypotensiveHypoxemia", Math.max(tSystolic, tSaturation)));
                break;
            }
        }

        return alerts;
    }
}
