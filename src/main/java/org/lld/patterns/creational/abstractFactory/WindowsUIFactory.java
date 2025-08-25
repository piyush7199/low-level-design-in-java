package org.lld.patterns.creational.abstractFactory;


import org.lld.patterns.creational.abstractFactory.product.Button;
import org.lld.patterns.creational.abstractFactory.product.Window;
import org.lld.patterns.creational.abstractFactory.product.impl.WindowButton;
import org.lld.patterns.creational.abstractFactory.product.impl.WindowWindow;

public class WindowsUIFactory implements UIFactory {
    public Button createButton() {
        return new WindowButton();
    }

    public Window createWindow() {
        return new WindowWindow();
    }
}
