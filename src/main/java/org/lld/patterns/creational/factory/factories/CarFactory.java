package org.lld.patterns.creational.factory.factories;


import org.lld.patterns.creational.factory.products.Car;
import org.lld.patterns.creational.factory.products.Vehicle;

public class CarFactory implements VehicleFactory {
    @Override
    public Vehicle createVehicle() {
        return new Car();
    }
}
