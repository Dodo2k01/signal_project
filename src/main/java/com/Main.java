package com;

import com.cardiogenerator.HealthDataSimulator;
import com.datamanagement.DataStorage;

public class Main {

        public static void main(String[] args) throws Exception {
            if (args.length > 0 && args[0].equals("DataStorage")) {
                DataStorage.main(new String[]{});
            } else {
                HealthDataSimulator.main(new String[]{});
            }
        }
    }


