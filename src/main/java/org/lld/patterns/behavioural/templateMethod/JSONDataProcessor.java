package org.lld.patterns.behavioural.templateMethod;

/**
 * Concrete implementation for processing JSON data.
 */
public class JSONDataProcessor extends DataProcessor {
    @Override
    protected void processDataInternal() {
        System.out.println("Processing JSON data: Parsing JSON, extracting fields...");
    }

    @Override
    protected void notifyCompletion() {
        System.out.println("JSON processing completed. Objects parsed successfully.");
    }
}

