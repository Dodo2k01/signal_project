package data_management;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.datamanagement.PatientRecord;

public class PatientRecordTest {

    @Test
    void testAllMethods() {
        PatientRecord patientRecord = new PatientRecord(1, 100.0, "WhiteBloodCells", 1714376789050L);

        int patientId = patientRecord.getPatientId();
        double measurementValue = patientRecord.getMeasurementValue();
        String recordType = patientRecord.getRecordType();
        long timestamp = patientRecord.getTimestamp();

        assertEquals(1, patientId);
        assertEquals(100.0, measurementValue);
        assertEquals("WhiteBloodCells", recordType);
        assertEquals(1714376789050L, timestamp);
    }   

}
