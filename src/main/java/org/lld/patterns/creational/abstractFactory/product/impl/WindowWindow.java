package org.lld.patterns.creational.abstractFactory.product.impl;

import org.lld.patterns.creational.abstractFactory.product.Window;

public class WindowWindow implements Window {
    public void display() {
        System.out.println("Displaying a Windows-style window");
    }
}