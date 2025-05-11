package com;

public class Util {

    public static Double parseDouble(String input) {
        try {
            // strip out any non-digit/decimal (e.g. “%”) first if you like:
            double measure = Double.parseDouble(input.replaceAll("[^0-9.–]", ""));
            return measure;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
