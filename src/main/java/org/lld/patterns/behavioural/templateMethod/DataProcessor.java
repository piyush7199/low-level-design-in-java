package org.lld.patterns.behavioural.templateMethod;

/**
 * Abstract class defining the template method and algorithm skeleton.
 * Subclasses implement specific steps but follow the same overall algorithm.
 */
public abstract class DataProcessor {
    /**
     * Template method - defines the algorithm skeleton.
     * This method is final to prevent subclasses from changing the algorithm structure.
     */
    public final void processData() {
        readData();
        processDataInternal();
        saveData();
        notifyCompletion();
    }

    /**
     * Step 1: Read data (common implementation)
     */
    private void readData() {
        System.out.println("Reading data from source...");
    }

    /**
     * Step 2: Process data (varies by subclass)
     * This is the hook method that subclasses must implement.
     */
    protected abstract void processDataInternal();

    /**
     * Step 3: Save data (common implementation)
     */
    private void saveData() {
        System.out.println("Saving processed data...");
    }

    /**
     * Step 4: Notify completion (optional hook - can be overridden)
     */
    protected void notifyCompletion() {
        System.out.println("Processing completed.");
    }
}

