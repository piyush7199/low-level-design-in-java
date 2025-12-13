package org.lld.patterns.behavioural.templateMethod;

/**
 * Concrete implementation for processing CSV data.
 */
public class CSVDataProcessor extends DataProcessor {
    @Override
    protected void processDataInternal() {
        System.out.println("Processing CSV data: Parsing rows, validating columns...");
    }

    @Override
    protected void notifyCompletion() {
        System.out.println("CSV processing completed. Rows processed successfully.");
    }
}

