package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@code FileOutputStrategy} class implements the {@link OutputStrategy} interface
 * and is responsible for outputting patient data to text files. It organizes data based on
 * labels and writes the data to corresponding files, creating directories and files as necessary.
 * The class uses a {@link ConcurrentHashMap} to store file paths associated with data labels.
 *
 * This class ensures that patient data is written in an organized and persistent manner,
 * allowing easy tracking of health data over time.
 *
 * <p>Note: The output files are created in the directory specified during the construction of the
 * {@code FileOutputStrategy} object, and the data is appended to existing files if they already exist.</p>
 *
 */
public class FileOutputStrategy implements OutputStrategy {

    private String BaseDirectory;

    public final ConcurrentHashMap<String, String> file_map = new ConcurrentHashMap<>();

    /**
     * Constructs a {@code FileOutputStrategy} with the specified base directory.
     * This directory is where all the output files will be created.
     *
     * @param baseDirectory The base directory where output files will be saved.
     */
    public FileOutputStrategy(String baseDirectory) {

        this.BaseDirectory = baseDirectory;
    }

    /**
     * Outputs the patient data to a file. If a file for the given label does not exist,
     * it will be created. The data is appended to the file if it already exists.
     *
     * @param patientId The ID of the patient whose data is being output.
     * @param timestamp The timestamp when the data was generated.
     * @param label A label categorizing the type of data.
     * @param data The actual data to be written to the file.
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(BaseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the FilePath variable
        String FilePath = file_map.computeIfAbsent(label, k -> Paths.get(BaseDirectory, label + ".txt").toString());

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(FilePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + FilePath + ": " + e.getMessage());
        }
    }
}
