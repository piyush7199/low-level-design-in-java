package org.lld.practice.design_parking_lot_system.improved_solution.strategies;

import org.lld.practice.design_parking_lot_system.improved_solution.models.ParkingSpot;

import java.util.List;
import java.util.Optional;

public interface SpotFindingStrategy {
    Optional<ParkingSpot> findSpot(List<ParkingSpot> availableSpots);
}
