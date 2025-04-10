package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Represents a generator for patient data in health monitoring simulations.
 * Implementing classes will generate simulated data for a specific patient,
 * which can be output using the specified {@link OutputStrategy}.
 *
 * @author Your Name
 */
public interface PatientDataGenerator {

    /**
     * Generates and outputs simulated data for a specific patient.
     *
     * @param patientId The ID of the patient for whom data is being generated.
     * @param outputStrategy The strategy used for outputting the generated data.
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
