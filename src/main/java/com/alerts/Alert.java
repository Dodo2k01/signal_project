package com.alerts;

import com.datamanagement.Patient;
import com.datamanagement.PatientRecord;

import java.util.*;
import java.util.stream.Collectors;

public class Alert {
    private int patientId;
    private String condition;
    private long timestamp;
    protected static final String[] differentTypes = {"Cholesterol", "DiastolicPressure", "RedBloodCells", "Saturation", "SystolicPressure", "WhiteBloodCells"};

    public Alert(int patientId, String condition, long timestamp) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Alert [patientId=" + patientId + ", condition=" + condition + ", timestamp=" + timestamp + "]";
    }

    public int getPatientId() {
        return patientId;
    }

    public String getCondition() {
        return condition;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public static class BloodPressureAlert extends AlertRule {

        @Override
        public List<Alert> evaluate(int patientID, List<PatientRecord> patientRecords) {
            List<Alert> alerts = new ArrayList<>();
            String[] types = {"SystolicPressure", "DiastolicPressure"};
            if (patientRecords.size() < 3) {
                for (String type : types) {
                    for (PatientRecord patientRecord : patientRecords) {
                        if (!type.equalsIgnoreCase(patientRecord.getRecordType())) continue;
                        Alert alert = checkCritical(patientRecord, type, patientID);
                        if (alert != null) {
                            alerts.add(alert);
                        }
                    }
                }
            }
            for (String type : types) {
                List<PatientRecord> typeRecords = getTypeRecords(patientRecords, type);
                for (int i = 2; i < typeRecords.size(); i++) {

                    ArrayList<PatientRecord> readings = new ArrayList<>();
                    readings.add(typeRecords.get(i));
                    readings.add(typeRecords.get(i - 1));
                    readings.add(typeRecords.get(i - 2));

                    double v0 = ((Number) readings.get(0).getMeasurementValue()).doubleValue(); // newest
                    Alert alert0 = checkCritical(typeRecords.get(0), type, patientID);
                    if (alert0 != null) {
                        alerts.add(alert0);
                    }
                    double v1 = ((Number) readings.get(1).getMeasurementValue()).doubleValue();
                    Alert alert1 = checkCritical(typeRecords.get(1), type, patientID);
                    if (alert1 != null) {
                        alerts.add(alert1);
                    }
                    double v2 = ((Number) readings.get(2).getMeasurementValue()).doubleValue(); // oldest
                    Alert alert2 = checkCritical(typeRecords.get(2), type, patientID);
                    if (alert2 != null) {
                        alerts.add(alert2);
                    }
                    long t0 = readings.get(0).getTimestamp();

                    if (v1 - v2 > 10 && v0 - v1 > 10) {
                        alerts.add(new Alert(patientID,
                                type + " trends up from: " + v2 + " to " + v0,
                                t0));
                    }
                    if (v1 - v2 < -10 && v0 - v1 < -10) {
                        alerts.add(new Alert(patientID,
                                type + " trends down from: " + v2 + " to " + v0,
                                t0));
                    }
                }
            }
            return alerts;
        }

        private Alert checkCritical(PatientRecord last, String type, int patientID) {
            try {
                double v = Double.parseDouble(last.getMeasurementValue().toString());
                long t = last.getTimestamp();
                if ("SystolicPressure".equals(type)) {
                    if (v > 180)
                        return new Alert(patientID,
                                "Systolic Pressure Critically High: " + v,
                                t);
                    if (v < 90)
                        return new Alert(patientID,
                                "Systolic Pressure Critically Low: " + v,
                                t);
                } else {
                    if (v > 120)
                        return new Alert(patientID,
                                "Diastolic Pressure Critically High: " + v,
                                t);
                    if (v < 60)
                        return new Alert(patientID,
                                "Diastolic Pressure Critically Low: " + v,
                                t);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return null;
        }

    }

    public static class BloodSaturationAlert extends AlertRule {
        private static final String saturation = "Saturation";

        @Override
        public List<Alert> evaluate(int id, List<PatientRecord> recs) {
            long min10 = 600000;
            List<Alert> alerts = new ArrayList<>();
            List<PatientRecord> saturationType = getTypeRecords(recs, saturation);
            if (saturationType.isEmpty()) return alerts;
            for (int i = saturationType.size() - 1; i > 0; i--) { //increment from the last index bc the newest records are last
                PatientRecord patientRecord = saturationType.get(i);
                long tNow = patientRecord.getTimestamp();
                double saturationNow = Double.parseDouble(patientRecord.getMeasurementValue().toString().replace("%", ""));
                if (saturationNow < 92)
                    alerts.add(new Alert(id, "Saturation critically low: " + saturationNow, patientRecord.getTimestamp()));
                for (int j = i - 1; j >= 0; j--) {
                    PatientRecord patientRecord1 = saturationType.get(j);
                    long tPrev = patientRecord1.getTimestamp();
                    double saturationPrev = Double.parseDouble(patientRecord1.getMeasurementValue().toString().replace("%", ""));
                    if ((tNow - tPrev) > min10) {
                        continue;
                    }
                    if (saturationPrev * 0.95 > saturationNow) {
                        alerts.add(new Alert(id, "Rapid Saturation drop from : " + saturationPrev + " to " + saturationNow, tNow));
                    }
                    if (j == 0 && saturationPrev < 92)
                        alerts.add(new Alert(id, "Saturation critically low: " + saturationNow, patientRecord.getTimestamp()));
                }
            }
            return alerts;
        }
    }

    public static class HypotensiveHypoxemiaAlert extends AlertRule {
        //I assume if both SystolicPressure and Saturation are below given levels within 10min
        // to each other them we create Alert
        @Override
        public List<Alert> evaluate(int patientID, List<PatientRecord> patientRecords) {
            List<Alert> alerts = new ArrayList<>();
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
                        alerts.add(new Alert(patientID, "HypotensiveHypoxemia", Math.max(tSystolic, tSaturation)));
                    break;
                }
            }
            return alerts;
        }

    }

    public static class ECGAlert extends AlertRule {
        @Override
        public List<Alert> evaluate(int patientID, List<PatientRecord> patientRecords) {
            List<Alert> alerts = new ArrayList<>();
            final int minRecords = 5;
            final long timeFrame = 60000;
            final double diffFromAvg = 0.25;
            for (int i = 0; i < differentTypes.length; i++) {
                String type = differentTypes[i];
                List<PatientRecord> records = getTypeRecords(patientRecords, type);
                if (records.isEmpty()) continue;
                PatientRecord firstRecord = records.get(0);
                int recordCount = records.size();
                Deque<PatientRecord> queue = new ArrayDeque<>();
                queue.add(firstRecord);
                long firstTimestamp = firstRecord.getTimestamp();
                long lastTimestamp;
                double mean = parseDouble(firstRecord.getMeasurementValue().toString());
                double sum = mean;
                for (int j = 1; j < recordCount; j++) {
                    double newValue = parseDouble(records.get(j).getMeasurementValue().toString());
                    lastTimestamp = records.get(j).getTimestamp();
                    // Evict old samples
                    while (!queue.isEmpty() && lastTimestamp - firstTimestamp > timeFrame) {
                        double value = parseDouble(queue.removeFirst().getMeasurementValue().toString());
                        sum -= value;
                        mean = queue.isEmpty() ? 0 : sum / queue.size();
                        if (!queue.isEmpty()) {
                            firstTimestamp = queue.peekFirst().getTimestamp();
                        } else {
                            firstTimestamp = lastTimestamp;
                        }
                    }

                    if (queue.size() >= minRecords) {
                        if (mean + (mean * diffFromAvg) < newValue) {
                            alerts.add(new Alert(
                                    patientID,
                                    "Unexpected Value Increase for " + type + " to " + newValue + " from the average of: " + mean,
                                    lastTimestamp
                            ));
                        } else if (mean - (mean * diffFromAvg) > newValue) {
                            alerts.add(new Alert(
                                    patientID,
                                    "Unexpected Value Decrease for " + type + " to " + newValue + " from the average of: " + mean,
                                    lastTimestamp
                            ));
                        }
                    }
                    sum += newValue;
                    queue.add(records.get(j));
                    mean = sum / queue.size();
                }
            }
            return alerts;
        }



        public static void main(String[] args) {
            ECGAlert rule = new ECGAlert();

            // 1) Test each type for a large baseline then a >25% spike (should alert)
            for (int idx = 0; idx < differentTypes.length; idx++) {
                String type = differentTypes[idx];
                Patient pInc = new Patient(100 + idx);

                // 10 steady readings at 100.0 every 5s
                for (int i = 0; i < 10; i++) {
                    pInc.addRecord(100.0, type, i * 5_000L);
                }
                // 11th reading jumps to 140.0 (40% ↑) at t=50s
                pInc.addRecord(140.0, type, 10 * 5_000L);

                System.out.println("Increase Test for " + type + ": " +
                        rule.evaluate(pInc.getPatientId(), pInc.getPatientRecords())
                );
            }

            // 2) Test each type for a large baseline then a >25% drop (should alert)
            for (int idx = 0; idx < differentTypes.length; idx++) {
                String type = differentTypes[idx];
                Patient pDec = new Patient(200 + idx);

                // 10 steady readings at 100.0 every 5s
                for (int i = 0; i < 10; i++) {
                    pDec.addRecord(100.0, type, i * 5_000L);
                }
                // 11th reading drops to  60.0 (40% ↓) at t=50s
                pDec.addRecord( 60.0, type, 10 * 5_000L);

                System.out.println("Decrease Test for " + type + ": " +
                        rule.evaluate(pDec.getPatientId(), pDec.getPatientRecords())
                );
            }

            // 3) Mixed‐type stream: only the spiking/dropping labels should fire
            Patient pmix = new Patient(999);
            long t = 0;
            // 5 baseline values for every type
            for (String type : differentTypes) {
                for (int i = 0; i < 5; i++) {
                    pmix.addRecord(50.0, type, t);
                    t += 1_000L;
                }
            }
            // add one 80.0 spike for the first type
            pmix.addRecord(80.0, differentTypes[0], t); t += 1_000L;
            // add one 10.0 drop for the second type
            pmix.addRecord(10.0, differentTypes[1], t);

            System.out.println("Mixed Types Test: " +
                    rule.evaluate(pmix.getPatientId(), pmix.getPatientRecords())
            );

            // 4) Out‐of‐window eviction test: spike but not enough records remain → no alerts
            Patient pout = new Patient(888);
            // 5 readings at t=0,10s,20s,30s,40s
            for (int i = 0; i < 5; i++) {
                pout.addRecord(100.0, differentTypes[2], i * 10_000L);
            }
            // at t=200s all 5 have aged out of the 60s window → window size<5
            pout.addRecord(200.0, differentTypes[2], 200_000L);

            System.out.println("Out‐of‐window Test: " +
                    rule.evaluate(pout.getPatientId(), pout.getPatientRecords())
            );
        }
    }

    public static class TriggeredAlert extends AlertRule {

        @Override
        public List<Alert> evaluate(int patientID, List<PatientRecord> patientRecords) {
            List<Alert> alerts = new ArrayList<>();
            String type = "Alert";
            List<PatientRecord> records = getTypeRecords(patientRecords, type);
            for (PatientRecord record : records) {
                if(record.getMeasurementValue().toString().equalsIgnoreCase(type))
                    alerts.add(new Alert(patientID, "Alert " + record.getMeasurementValue().toString(), record.getTimestamp()));
            }
            return alerts;
        }
    }
}



