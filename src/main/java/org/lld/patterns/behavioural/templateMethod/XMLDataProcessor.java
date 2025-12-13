package org.lld.patterns.behavioural.templateMethod;

/**
 * Concrete implementation for processing XML data.
 */
public class XMLDataProcessor extends DataProcessor {
    @Override
    protected void processDataInternal() {
        System.out.println("Processing XML data: Parsing XML, validating schema...");
    }
}

