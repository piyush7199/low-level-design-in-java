package org.lld.patterns.creational.abstractFactory;


import org.lld.patterns.creational.abstractFactory.product.Button;
import org.lld.patterns.creational.abstractFactory.product.Window;
import org.lld.patterns.creational.abstractFactory.product.impl.MacButton;
import org.lld.patterns.creational.abstractFactory.product.impl.MacWindow;

public class MacUIFactory implements UIFactory {
    public Button createButton() {
        return new MacButton();
    }

    public Window createWindow() {
        return new MacWindow();
    }
}
