package org.lld.practice.design_food_delivery_system.improved_solution.strategies;

import org.lld.practice.design_food_delivery_system.improved_solution.models.DeliveryPartner;
import org.lld.practice.design_food_delivery_system.improved_solution.models.Location;

import java.util.List;

public class NearestPartnerStrategy implements DeliveryAssignmentStrategy {
    
    @Override
    public DeliveryPartner assignPartner(List<DeliveryPartner> availablePartners, Location restaurantLocation) {
        if (availablePartners.isEmpty()) {
            return null;
        }

        DeliveryPartner nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (DeliveryPartner partner : availablePartners) {
            double distance = partner.getCurrentLocation().distanceTo(restaurantLocation);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = partner;
            }
        }

        return nearest;
    }
}

