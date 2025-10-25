package org.lld.practice.design_food_delivery_system.improved_solution.strategies;

import org.lld.practice.design_food_delivery_system.improved_solution.models.DeliveryPartner;
import org.lld.practice.design_food_delivery_system.improved_solution.models.Location;

import java.util.List;

public interface DeliveryAssignmentStrategy {
    DeliveryPartner assignPartner(List<DeliveryPartner> availablePartners, Location restaurantLocation);
}

