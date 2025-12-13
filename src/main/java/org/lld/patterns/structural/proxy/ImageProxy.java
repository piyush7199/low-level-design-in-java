package org.lld.patterns.structural.proxy;

/**
 * Proxy class that controls access to RealImage.
 * Implements lazy loading - only creates RealImage when display() is called.
 */
public class ImageProxy implements Image {
    private final String fileName;
    private RealImage realImage;

    public ImageProxy(String fileName) {
        this.fileName = fileName;
        System.out.println("Proxy created for: " + fileName + " (image not loaded yet)");
    }

    @Override
    public void display() {
        // Lazy initialization: create RealImage only when needed
        if (realImage == null) {
            realImage = new RealImage(fileName);
        }
        realImage.display();
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    /**
     * Check if the image has been loaded.
     */
    public boolean isLoaded() {
        return realImage != null;
    }
}

