package com.alerts.alert_decorators;

import com.alerts.alert_factories.AlertFactory;
import com.alerts.alerts.Alert;

public class PriorityAlertDecorator implements AlertFactory {
    Alert alert;

    public PriorityAlertDecorator(Alert alert) {
        this.alert = alert;
    }


    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        alert.setCondition("PRIORITY: " + condition);
        return alert;
    }
}
