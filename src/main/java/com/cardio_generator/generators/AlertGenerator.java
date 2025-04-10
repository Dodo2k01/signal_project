package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * The {@code AlertGenerator} class generates simulated alerts for multiple patients.
 * It triggers alerts with a given probability and resolves them with a high probability.
 * Alerts are represented by a state (triggered or resolved) and are output using
 * the provided {@link OutputStrategy}.
 *
 * @author Your Name
 */
public class AlertGenerator implements PatientDataGenerator {

    public static final Random randomGenerator = new Random();
    private boolean[] alertStates; // false = resolved, true = triggered
    //changed all variable names AlertStates to alertStates for correct convention
    /**
     * Constructs an {@code AlertGenerator} for the specified number of patients.
     * Initializes the alert states to "resolved" for each patient.
     *
     * @param patientCount The number of patients for which alerts will be generated.
     */
    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1];
    }

    /**
     * Generates and outputs an alert for a specific patient. The alert can either be triggered
     * or resolved based on predefined probabilities.
     *
     * @param patientId The ID of the patient for whom the alert is generated.
     * @param outputStrategy The strategy used to output the generated alert data.
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (alertStates[patientId]) {
                if (randomGenerator.nextDouble() < 0.9) { // 90% chance to resolve
                    alertStates[patientId] = false;
                    // Output the alert resolution
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                double lambda = 0.1; // Average rate of alert generation
                //again changed Lambda to lambda for correct convention

                double p = -Math.expm1(-lambda); // Probability of an alert being triggered
                boolean alertTriggered = randomGenerator.nextDouble() < p;

                if (alertTriggered) {
                    alertStates[patientId] = true;
                    // Output the alert trigger
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}
