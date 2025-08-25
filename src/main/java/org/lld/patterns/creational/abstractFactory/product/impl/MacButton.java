package org.lld.patterns.creational.abstractFactory.product.impl;


import org.lld.patterns.creational.abstractFactory.product.Button;

public class MacButton implements Button {
    public void render() {
        System.out.println("Rendering a Mac-style button");
    }
}