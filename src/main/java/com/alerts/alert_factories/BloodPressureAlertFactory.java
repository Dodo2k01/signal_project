package com.alerts.alert_factories;

import com.alerts.alerts.BloodPressureAlert;

public class BloodPressureAlertFactory extends AlertFactory {

    public BloodPressureAlert createAlert(int patientId, String condition, long timestamp) {
        
        BloodPressureAlert alert = new BloodPressureAlert(patientId, condition, timestamp);

        return alert;
    }
}
