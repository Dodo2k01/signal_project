package com.alerts.alert_factories;

import com.alerts.alerts.ECGAlert;

public class ECGAlertFactory extends AlertFactory {

    public ECGAlert createAlert(int patientId, String condition, long timestamp) {
        
        ECGAlert alert = new ECGAlert(patientId, condition, timestamp);

        return alert;
    }
}
