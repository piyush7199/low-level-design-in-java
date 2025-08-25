package org.lld.patterns.creational.abstractFactory;

import org.lld.patterns.creational.abstractFactory.product.Button;
import org.lld.patterns.creational.abstractFactory.product.Window;

public interface UIFactory {
    Button createButton();

    Window createWindow();
}
