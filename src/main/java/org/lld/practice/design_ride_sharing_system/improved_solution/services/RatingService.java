package org.lld.practice.design_ride_sharing_system.improved_solution.services;

import org.lld.practice.design_ride_sharing_system.improved_solution.models.Driver;
import org.lld.practice.design_ride_sharing_system.improved_solution.models.Rider;

public class RatingService {
    
    public void rateDriver(Driver driver, double rating) {
        if (rating < 1.0 || rating > 5.0) {
            System.out.println("[RatingService] Invalid rating. Must be between 1.0 and 5.0");
            return;
        }
        driver.updateRating(rating);
        System.out.println("[RatingService] Driver " + driver.getName() + 
                         " rated: " + rating + ". New average: " + 
                         String.format("%.2f", driver.getRating()));
    }

    public void rateRider(Rider rider, double rating) {
        if (rating < 1.0 || rating > 5.0) {
            System.out.println("[RatingService] Invalid rating. Must be between 1.0 and 5.0");
            return;
        }
        rider.updateRating(rating);
        System.out.println("[RatingService] Rider " + rider.getName() + 
                         " rated: " + rating + ". New average: " + 
                         String.format("%.2f", rider.getRating()));
    }
}

