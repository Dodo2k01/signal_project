package data_management;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.datamanagement.PatientRecord;

import com.alerts.Alert;

public class AlertTest {

    @Test
    void testAllMethods() {
        Alert alert = new Alert(1, "Traumatic Brain Injury", 1714376789050L);

        int patientId = alert.getPatientId();
        String condition = alert.getCondition();
        long timestamp = alert.getTimestamp();

        assertEquals(1, patientId);
        assertEquals("Traumatic Brain Injury", condition);
        assertEquals(1714376789050L, timestamp);
        
    }
}
