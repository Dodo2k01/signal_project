package com.alerts.alert_rules;

import com.alerts.Alert;
import com.datamanagement.PatientRecord;

import java.util.List;

public class BloodSaturationAlert extends Alert implements AlertRule {
    private static final String saturation = "Saturation";
    public BloodSaturationAlert(String patientId, String condition, long timestamp) {
        super(Integer.parseInt(patientId), condition, timestamp);
    }

    @Override
    public Alert evaluate(int id, List<PatientRecord> recs) {

        List<PatientRecord> s = getLastRecords(recs, saturation, 2);

        if (s.isEmpty()) return null;

        double now = Double.parseDouble(s.get(0).getMeasurementValue().toString().replace("%",""));
        long   t   = s.get(0).getTimestamp();

        if (now < 92)                                   // low saturation
            return new BloodSaturationAlert(id+"", "SAT_LOW", t);

        if (s.size()==2 && t - s.get(1).getTimestamp() <= 600_000) {        // 10 min
            double prev = Double.parseDouble(s.get(1).getMeasurementValue().toString().replace("%",""));
            if (prev - now >= 5)                                            // rapid drop ≥ 5 %
                return new BloodSaturationAlert(id+"", "SAT_DROP", t);
        }
        return null;
    }

}
