package org.lld.practice.design_queue_management_system.improved_solution.observers;

import org.lld.practice.design_queue_management_system.improved_solution.models.Counter;
import org.lld.practice.design_queue_management_system.improved_solution.models.Customer;
import org.lld.practice.design_queue_management_system.improved_solution.models.Token;

import java.util.List;

/**
 * Observer that handles customer notifications via SMS/Email.
 * Notifies customers when their token is generated or called.
 */
public class CustomerNotificationObserver implements QueueObserver {
    
    private static final int NOTIFY_WHEN_AHEAD = 3; // Notify when 3 tokens ahead

    @Override
    public void onTokenGenerated(Token token) {
        Customer customer = token.getCustomer();
        sendSMS(customer.getPhoneNumber(), 
                String.format("Your token %s has been generated. Please wait for your turn.", 
                        token.getTokenNumber()));
    }

    @Override
    public void onTokenCalled(Token token, Counter counter) {
        Customer customer = token.getCustomer();
        sendSMS(customer.getPhoneNumber(),
                String.format("🔔 Your token %s is being called! Please proceed to %s", 
                        token.getTokenNumber(), counter.getCounterName()));
        
        // Could also send email
        if (customer.getEmail() != null) {
            sendEmail(customer.getEmail(), "Your Turn!",
                    String.format("Please proceed to %s for service.", counter.getCounterName()));
        }
    }

    @Override
    public void onTokenCompleted(Token token) {
        Customer customer = token.getCustomer();
        sendSMS(customer.getPhoneNumber(),
                String.format("Thank you for visiting! Your service for token %s is complete.", 
                        token.getTokenNumber()));
    }

    @Override
    public void onTokenCancelled(Token token) {
        Customer customer = token.getCustomer();
        sendSMS(customer.getPhoneNumber(),
                String.format("Your token %s has been cancelled.", token.getTokenNumber()));
    }

    @Override
    public void onQueueUpdated(List<Token> waitingTokens) {
        // Notify customers who are about to be called
        for (int i = 0; i < Math.min(NOTIFY_WHEN_AHEAD, waitingTokens.size()); i++) {
            Token token = waitingTokens.get(i);
            if (i == NOTIFY_WHEN_AHEAD - 1) {
                sendSMS(token.getCustomer().getPhoneNumber(),
                        String.format("Your token %s will be called soon. %d customers ahead.",
                                token.getTokenNumber(), i));
            }
        }
    }
    
    /**
     * Simulate sending SMS (in real implementation, integrate with SMS gateway).
     */
    private void sendSMS(String phoneNumber, String message) {
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            System.out.printf("📱 SMS to %s: %s%n", phoneNumber, message);
        }
    }
    
    /**
     * Simulate sending email (in real implementation, integrate with email service).
     */
    private void sendEmail(String email, String subject, String body) {
        if (email != null && !email.isEmpty()) {
            System.out.printf("📧 Email to %s: [%s] %s%n", email, subject, body);
        }
    }
}

