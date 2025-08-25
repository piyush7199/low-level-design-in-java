package org.lld.patterns.creational.abstractFactory.product.impl;

import org.lld.patterns.creational.abstractFactory.product.Button;

public class WindowButton implements Button {
    public void render() {
        System.out.println("Rendering a Windows-style button");
    }
}