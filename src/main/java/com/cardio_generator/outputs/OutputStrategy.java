package com.cardio_generator.outputs;

/**
 * Represents an output strategy for health data in health monitoring simulations.
 * Implementing classes will define how to output the generated data, which could
 * include console output, file output, or network-based output.
 *
 * @author Your Name
 */
public interface OutputStrategy {

    /**
     * Outputs the generated data for a specific patient.
     *
     * @param patientId The ID of the patient whose data is being output.
     * @param timestamp The timestamp when the data was generated.
     * @param label A label categorizing the type of data.
     * @param data The actual data to be output.
     */
    void output(int patientId, long timestamp, String label, String data);
}
