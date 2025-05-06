package data_management;

import com.alerts.Alert;
import com.datamanagement.Patient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

//BloodPressureAlert tests
//// main class I used to check

//Alert.BloodPressureAlert rule = new Alert.BloodPressureAlert();
//
//// 1) Systolic critical high
//Patient p1 = new Patient(1);
//            p1.addRecord(200.0, "SystolicPressure", 1L);
//            System.out.println("Sys High → " + rule.evaluate(p1.getPatientId(), p1.getPatientRecords()));
//
//// 2) Systolic critical low
//Patient p2 = new Patient(2);
//            p2.addRecord(80.0, "SystolicPressure", 2L);
//            System.out.println("Sys Low  → " + rule.evaluate(p2.getPatientId(), p2.getPatientRecords()));
//
//// 3) Diastolic critical high
//Patient p3 = new Patient(3);
//            p3.addRecord(131.0, "DiastolicPressure", 3L);
//            System.out.println("Dia High → " + rule.evaluate(p3.getPatientId(), p3.getPatientRecords()));
//
//// 4) Diastolic critical low
//Patient p4 = new Patient(4);
//            p4.addRecord(49.0, "DiastolicPressure", 4L);
//            System.out.println("Dia Low  → " + rule.evaluate(p4.getPatientId(), p4.getPatientRecords()));
//
//long t = 5L;
//// 5) Systolic trend up: 100 → 115 → 130
//Patient p5 = new Patient(5);
//            p5.addRecord(100.0, "SystolicPressure", t++);
//            p5.addRecord(115.0, "SystolicPressure", t++);
//            p5.addRecord(130.0, "SystolicPressure", t++);
//            System.out.println("Sys Trend Up   → " + rule.evaluate(p5.getPatientId(), p5.getPatientRecords()));
//
//// 6) Systolic trend down: 130 → 115 → 100
//Patient p6 = new Patient(6);
//            p6.addRecord(130.0, "SystolicPressure", t++);
//            p6.addRecord(115.0, "SystolicPressure", t++);
//            p6.addRecord(100.0, "SystolicPressure", t++);
//            System.out.println("Sys Trend Down → " + rule.evaluate(p6.getPatientId(), p6.getPatientRecords()));
//
//// 7) Diastolic trend up: 50 → 65 → 80
//Patient p7 = new Patient(7);
//            p7.addRecord(61.0, "DiastolicPressure", t++);
//            p7.addRecord(72.0, "DiastolicPressure", t++);
//            p7.addRecord(83.0, "DiastolicPressure", t++);
//            System.out.println("Dia Trend Up   → " + rule.evaluate(p7.getPatientId(), p7.getPatientRecords()));
//
//// 8) Diastolic trend down: 80 → 65 → 50
//Patient p8 = new Patient(8);
//            p8.addRecord(100.0, "DiastolicPressure", t++);
//            p8.addRecord(89.0, "DiastolicPressure", t++);
//            p8.addRecord(78.0, "DiastolicPressure", t++);
//            System.out.println("Dia Trend Down → " + rule.evaluate(p8.getPatientId(), p8.getPatientRecords()));
//        }

//BloodSaturationAlert
//// Main class i used:
//Alert.BloodSaturationAlert rule = new Alert.BloodSaturationAlert();
//
//// 1) Low saturation only
//Patient p1 = new Patient(1);
//        p1.addRecord("95%", "Saturation", 1L);    // no alert
//        p1.addRecord("91%", "Saturation", 2L);    // low → alert
//        System.out.println("Low Sat Test:    " +
//                           rule.evaluate(p1.getPatientId(), p1.getPatientRecords())
//        );
//
//// 2) Rapid drop only
//Patient p2 = new Patient(2);
//        p2.addRecord("98%", "Saturation", 10L);
//        p2.addRecord("92%", "Saturation", 10L + 5 * 60 * 1000); // 5 min later, drop of 6%
//        System.out.println("Rapid Drop Test: " +
//                           rule.evaluate(p2.getPatientId(), p2.getPatientRecords())
//        );
//
//// 3) Both combined
//Patient p3 = new Patient(3);
//        p3.addRecord("97%", "Saturation", 100L);
//        p3.addRecord("90%", "Saturation", 100L + 8 * 60 * 1000); // 8 min later, drop of 7%
//        System.out.println("Combined Test:   " +
//                           rule.evaluate(p3.getPatientId(), p3.getPatientRecords())
//        );


////HypotensiveHypoxemiaAlert
///main method i used
//Alert.HypotensiveHypoxemiaAlert rule = new Alert.HypotensiveHypoxemiaAlert();
//
//
//// Case 1: neither condition → no alerts
//Patient p1 = new Patient(1);
//            p1.addRecord(120.0, "SystolicPressure", 1L);
//            p1.addRecord("95%", "Saturation", 1L);
//            System.out.println("Case 1 (none): " +
//                               rule.evaluate(p1.getPatientId(), p1.getPatientRecords())
//        );
//
//// Case 4: both within 10 min → one alert
//Patient p4 = new Patient(4);
//            p4.addRecord("91%", "Saturation", 10_000L);
//            p4.addRecord(88.0, "SystolicPressure", 15_000L);   // 5 sec later
//            System.out.println("Case 4 (both within 10 min): " +
//                               rule.evaluate(p4.getPatientId(), p4.getPatientRecords())
//        );
//
//// Case 5: both but >10 min apart → no alert
//Patient p5 = new Patient(5);
//            p5.addRecord("90%", "Saturation", 20_000L);
//            p5.addRecord(85.0, "SystolicPressure", 15_000L - 700_000L); // >10 min before
//            System.out.println("Case 5 (both >10 min apart): " +
//                               rule.evaluate(p5.getPatientId(), p5.getPatientRecords())
//        );
//
//// Case 6: multiple matching pairs → one alert per low‐sat reading
//Patient p6 = new Patient(6);
//            p6.addRecord("89%", "Saturation", 30_000L);
//            p6.addRecord(92.0, "SystolicPressure", 31_000L);
//            p6.addRecord(87.0, "SystolicPressure", 35_000L);   // second low‐BP
//            System.out.println("Case 6 (multiple matches): " +
//                               rule.evaluate(p6.getPatientId(), p6.getPatientRecords())
//        );
//        }

//// ECG rule main
//public static void main(String[] args) {
//    Alert.ECGAlert rule = new Alert.ECGAlert();
//
//    // 1) Test each type for a large baseline then a >25% spike (should alert)
//    for (int idx = 0; idx < differentTypes.length; idx++) {
//        String type = differentTypes[idx];
//        Patient pInc = new Patient(100 + idx);
//
//        // 10 steady readings at 100.0 every 5s
//        for (int i = 0; i < 10; i++) {
//            pInc.addRecord(100.0, type, i * 5_000L);
//        }
//        // 11th reading jumps to 140.0 (40% ↑) at t=50s
//        pInc.addRecord(140.0, type, 10 * 5_000L);
//
//        System.out.println("Increase Test for " + type + ": " +
//                rule.evaluate(pInc.getPatientId(), pInc.getPatientRecords())
//        );
//    }
//
//    // 2) Test each type for a large baseline then a >25% drop (should alert)
//    for (int idx = 0; idx < differentTypes.length; idx++) {
//        String type = differentTypes[idx];
//        Patient pDec = new Patient(200 + idx);
//
//        // 10 steady readings at 100.0 every 5s
//        for (int i = 0; i < 10; i++) {
//            pDec.addRecord(100.0, type, i * 5_000L);
//        }
//        // 11th reading drops to  60.0 (40% ↓) at t=50s
//        pDec.addRecord( 60.0, type, 10 * 5_000L);
//
//        System.out.println("Decrease Test for " + type + ": " +
//                rule.evaluate(pDec.getPatientId(), pDec.getPatientRecords())
//        );
//    }
//
//    // 3) Mixed‐type stream: only the spiking/dropping labels should fire
//    Patient pmix = new Patient(999);
//    long t = 0;
//    // 5 baseline values for every type
//    for (String type : differentTypes) {
//        for (int i = 0; i < 5; i++) {
//            pmix.addRecord(50.0, type, t);
//            t += 1_000L;
//        }
//    }
//    // add one 80.0 spike for the first type
//    pmix.addRecord(80.0, differentTypes[0], t); t += 1_000L;
//    // add one 10.0 drop for the second type
//    pmix.addRecord(10.0, differentTypes[1], t);
//
//    System.out.println("Mixed Types Test: " +
//            rule.evaluate(pmix.getPatientId(), pmix.getPatientRecords())
//    );
//
//    // 4) Out‐of‐window eviction test: spike but not enough records remain → no alerts
//    Patient pout = new Patient(888);
//    // 5 readings at t=0,10s,20s,30s,40s
//    for (int i = 0; i < 5; i++) {
//        pout.addRecord(100.0, differentTypes[2], i * 10_000L);
//    }
//    // at t=200s all 5 have aged out of the 60s window → window size<5
//    pout.addRecord(200.0, differentTypes[2], 200_000L);
//
//    System.out.println("Out‐of‐window Test: " +
//            rule.evaluate(pout.getPatientId(), pout.getPatientRecords())
//    );
//}
