package org.lld.practice.design_food_delivery_system.improved_solution;

import org.lld.practice.design_food_delivery_system.improved_solution.models.*;
import org.lld.practice.design_food_delivery_system.improved_solution.services.*;
import org.lld.practice.design_food_delivery_system.improved_solution.strategies.NearestPartnerStrategy;

import java.util.List;

/**
 * Facade for the food delivery system
 */
public class FoodDeliverySystem {
    private final UserService userService;
    private final RestaurantService restaurantService;
    private final OrderService orderService;
    private final DeliveryService deliveryService;
    private final PaymentService paymentService;
    private final NotificationService notificationService;

    public FoodDeliverySystem() {
        this.userService = new UserService();
        this.restaurantService = new RestaurantService();
        this.notificationService = new NotificationService();
        this.deliveryService = new DeliveryService(new NearestPartnerStrategy());
        this.paymentService = new PaymentService();
        this.orderService = new OrderService(restaurantService, deliveryService, paymentService, notificationService);
    }

    public void registerCustomer(Customer customer) {
        userService.registerCustomer(customer);
    }

    public void registerRestaurant(Restaurant restaurant) {
        restaurantService.addRestaurant(restaurant);
    }

    public void registerDeliveryPartner(DeliveryPartner partner) {
        deliveryService.registerPartner(partner);
    }

    public Order placeOrder(String customerId, String restaurantId, List<MenuItem> items) {
        return orderService.placeOrder(customerId, restaurantId, items);
    }

    public void updateOrderStatus(String orderId, OrderStatus newStatus) {
        orderService.updateOrderStatus(orderId, newStatus);
    }

    public List<Restaurant> searchRestaurants(String query) {
        return restaurantService.searchRestaurants(query);
    }

    public Order getOrder(String orderId) {
        return orderService.getOrder(orderId);
    }

    public OrderService getOrderService() {
        return orderService;
    }
}

