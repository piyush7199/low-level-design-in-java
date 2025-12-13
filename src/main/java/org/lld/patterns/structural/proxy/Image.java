package org.lld.patterns.structural.proxy;

/**
 * Subject interface that both RealImage and ProxyImage implement.
 */
public interface Image {
    void display();
    String getFileName();
}

