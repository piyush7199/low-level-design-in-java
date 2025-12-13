package org.lld.patterns.behavioural.templateMethod;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Template Method Pattern Demo ===\n");

        System.out.println("1. Processing CSV Data:");
        DataProcessor csvProcessor = new CSVDataProcessor();
        csvProcessor.processData();

        System.out.println("\n2. Processing JSON Data:");
        DataProcessor jsonProcessor = new JSONDataProcessor();
        jsonProcessor.processData();

        System.out.println("\n3. Processing XML Data:");
        DataProcessor xmlProcessor = new XMLDataProcessor();
        xmlProcessor.processData();

        System.out.println("\nNotice: All processors follow the same algorithm structure,");
        System.out.println("        but implement different processing logic!");
    }
}

