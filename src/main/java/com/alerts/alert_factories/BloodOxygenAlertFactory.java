package com.alerts.alert_factories;

import com.alerts.alerts.BloodOxygenAlert;

public class BloodOxygenAlertFactory extends AlertFactory {

    public BloodOxygenAlert createAlert(int patientId, String condition, long timestamp) {
        
        BloodOxygenAlert alert = new BloodOxygenAlert(patientId, condition, timestamp);

        return alert;
    }
}
