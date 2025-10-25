package org.lld.practice.design_food_delivery_system.improved_solution.services;

import org.lld.practice.design_food_delivery_system.improved_solution.models.DeliveryPartner;
import org.lld.practice.design_food_delivery_system.improved_solution.models.Location;
import org.lld.practice.design_food_delivery_system.improved_solution.strategies.DeliveryAssignmentStrategy;

import java.util.*;

public class DeliveryService {
    private final Map<String, DeliveryPartner> deliveryPartners;
    private final DeliveryAssignmentStrategy assignmentStrategy;

    public DeliveryService(DeliveryAssignmentStrategy assignmentStrategy) {
        this.deliveryPartners = new HashMap<>();
        this.assignmentStrategy = assignmentStrategy;
    }

    public void registerPartner(DeliveryPartner partner) {
        deliveryPartners.put(partner.getUserId(), partner);
        System.out.println("[DeliveryService] Delivery partner registered: " + partner.getName());
    }

    public DeliveryPartner assignDeliveryPartner(Location restaurantLocation, Location customerLocation) {
        List<DeliveryPartner> availablePartners = new ArrayList<>();
        for (DeliveryPartner partner : deliveryPartners.values()) {
            if (partner.isAvailable()) {
                availablePartners.add(partner);
            }
        }

        DeliveryPartner assignedPartner = assignmentStrategy.assignPartner(availablePartners, restaurantLocation);
        
        if (assignedPartner != null) {
            assignedPartner.setAvailable(false);
            System.out.println("[DeliveryService] Assigned delivery partner: " + assignedPartner.getName());
        } else {
            System.out.println("[DeliveryService] No available delivery partners");
        }
        
        return assignedPartner;
    }

    public DeliveryPartner getPartner(String partnerId) {
        return deliveryPartners.get(partnerId);
    }
}

