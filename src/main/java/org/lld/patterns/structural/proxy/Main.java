package org.lld.patterns.structural.proxy;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Proxy Pattern Demo ===\n");

        // Create proxy objects (images are NOT loaded yet)
        System.out.println("1. Creating proxy objects:");
        Image image1 = new ImageProxy("photo1.jpg");
        Image image2 = new ImageProxy("photo2.jpg");
        Image image3 = new ImageProxy("photo3.jpg");

        System.out.println("\n2. Proxies created, but images not loaded yet.");
        System.out.println("   Image1 loaded: " + ((ImageProxy) image1).isLoaded());
        System.out.println("   Image2 loaded: " + ((ImageProxy) image2).isLoaded());

        // Only now will the image be loaded (lazy loading)
        System.out.println("\n3. Displaying image1 (triggers loading):");
        image1.display();

        System.out.println("\n4. Displaying image1 again (already loaded, no reload):");
        image1.display();

        System.out.println("\n5. Displaying image2 (triggers loading):");
        image2.display();

        System.out.println("\n6. Image3 proxy exists but image not loaded:");
        System.out.println("   Image3 loaded: " + ((ImageProxy) image3).isLoaded());
    }
}

