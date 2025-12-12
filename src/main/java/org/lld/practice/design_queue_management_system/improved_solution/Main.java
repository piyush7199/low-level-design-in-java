package org.lld.practice.design_queue_management_system.improved_solution;

import org.lld.practice.design_queue_management_system.improved_solution.models.Counter;
import org.lld.practice.design_queue_management_system.improved_solution.models.Customer;
import org.lld.practice.design_queue_management_system.improved_solution.models.Token;
import org.lld.practice.design_queue_management_system.improved_solution.models.TokenType;
import org.lld.practice.design_queue_management_system.improved_solution.strategies.PriorityAssignmentStrategy;

import java.util.Arrays;
import java.util.List;

/**
 * Demo application for the Queue Management System.
 * 
 * Demonstrates:
 * - Token generation with different types
 * - Priority-based token assignment
 * - Counter management
 * - Display board updates
 * - Analytics tracking
 */
public class Main {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║     🏦 QUEUE MANAGEMENT SYSTEM - DEMO                          ║");
        System.out.println("║     Design Patterns: Singleton, Strategy, Observer, State     ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝\n");
        
        // Reset any previous instance (for clean demo)
        QueueManagementSystem.resetInstance();
        
        // Initialize system with Priority-based assignment strategy
        QueueManagementSystem qms = QueueManagementSystem.getInstance(
                new PriorityAssignmentStrategy()
        );
        
        // ========== Step 1: Setup Counters ==========
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("STEP 1: Setting up Service Counters");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        // Counter 1: Serves all token types
        Counter counter1 = new Counter("C1", "Counter 1 - General", 
                Arrays.asList(TokenType.REGULAR, TokenType.SENIOR_CITIZEN, TokenType.PRIORITY, TokenType.VIP));
        
        // Counter 2: VIP and Priority only
        Counter counter2 = new Counter("C2", "Counter 2 - Priority", 
                Arrays.asList(TokenType.PRIORITY, TokenType.VIP));
        
        // Counter 3: Regular and Senior Citizens
        Counter counter3 = new Counter("C3", "Counter 3 - Regular", 
                Arrays.asList(TokenType.REGULAR, TokenType.SENIOR_CITIZEN));
        
        qms.addCounter(counter1);
        qms.addCounter(counter2);
        qms.addCounter(counter3);
        
        // Open counters with operators
        qms.openCounter("C1", "Alice");
        qms.openCounter("C2", "Bob");
        qms.openCounter("C3", "Charlie");
        
        System.out.println();
        
        // ========== Step 2: Generate Tokens ==========
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("STEP 2: Generating Tokens for Customers");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        // Create customers and generate tokens
        Customer john = new Customer("CUST001", "John Doe", "555-0101");
        Customer jane = new Customer("CUST002", "Jane Smith", "555-0102", "jane@email.com");
        Customer bob = new Customer("CUST003", "Bob Wilson", "555-0103");
        Customer alice = new Customer("CUST004", "Alice Brown", "555-0104");
        Customer senior = new Customer("CUST005", "George Senior", "555-0105");
        Customer vip = new Customer("CUST006", "Victoria VIP", "555-0106", "vip@email.com");
        
        // Generate tokens in specific order
        Token t1 = qms.generateToken(john, TokenType.REGULAR);
        Thread.sleep(100); // Small delay to simulate time passing
        
        Token t2 = qms.generateToken(jane, TokenType.REGULAR);
        Thread.sleep(100);
        
        Token t3 = qms.generateToken(bob, TokenType.PRIORITY);
        Thread.sleep(100);
        
        Token t4 = qms.generateToken(senior, TokenType.SENIOR_CITIZEN);
        Thread.sleep(100);
        
        Token t5 = qms.generateToken(alice, TokenType.REGULAR);
        Thread.sleep(100);
        
        Token t6 = qms.generateToken(vip, TokenType.VIP);
        
        System.out.println("\n📋 Current Queue Status:");
        System.out.printf("   Waiting tokens: %d%n", qms.getWaitingCount());
        printQueue(qms.getWaitingQueue());
        
        // ========== Step 3: Call Tokens (Priority-based) ==========
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("STEP 3: Calling Tokens (Notice Priority Order!)");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        System.out.println(">>> Counter C1 calling next token...");
        qms.callNextToken("C1");
        Thread.sleep(500);
        
        System.out.println("\n>>> Counter C2 calling next token...");
        qms.callNextToken("C2");
        Thread.sleep(500);
        
        System.out.println("\n>>> Counter C3 calling next token...");
        qms.callNextToken("C3");
        
        System.out.println("\n📋 Remaining Queue:");
        printQueue(qms.getWaitingQueue());
        
        // ========== Step 4: Complete Services ==========
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("STEP 4: Completing Services");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        Thread.sleep(1000); // Simulate service time
        
        System.out.println(">>> Completing service at Counter C1...");
        qms.completeService("C1");
        
        System.out.println("\n>>> Counter C1 calling next token...");
        qms.callNextToken("C1");
        
        Thread.sleep(500);
        
        System.out.println("\n>>> Completing service at Counter C2...");
        qms.completeService("C2");
        
        // ========== Step 5: Handle No-Show ==========
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("STEP 5: Handling No-Show");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        System.out.println(">>> Customer at Counter C3 didn't show up...");
        qms.markNoShow("C3");
        
        System.out.println("\n>>> Counter C3 calling next token...");
        qms.callNextToken("C3");
        
        // ========== Step 6: Cancel Token ==========
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("STEP 6: Token Cancellation");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        // Complete remaining services first
        qms.completeService("C1");
        qms.completeService("C3");
        
        // ========== Step 7: Analytics Report ==========
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("STEP 7: Analytics Report");
        System.out.println("═══════════════════════════════════════════════════════════════");
        
        qms.printAnalyticsReport();
        
        // ========== Summary ==========
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO COMPLETE - KEY DESIGN PATTERNS DEMONSTRATED:");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("✅ Singleton: QueueManagementSystem - single instance");
        System.out.println("✅ Strategy: PriorityAssignmentStrategy - VIP served before Regular");
        System.out.println("✅ Observer: DisplayBoard, Notifications, Analytics - decoupled updates");
        System.out.println("✅ State: Token lifecycle (Waiting → Serving → Completed/NoShow)");
        System.out.println("✅ SOLID: Separation of concerns across Services, Strategies, Observers");
        System.out.println("\n🎯 Notice: VIP token (V-001) was served before Regular tokens (R-001, R-002)");
        System.out.println("   even though Regular customers arrived first!");
    }
    
    private static void printQueue(List<Token> queue) {
        if (queue.isEmpty()) {
            System.out.println("   [Queue is empty]");
            return;
        }
        
        System.out.println("   Position | Token  | Type           | Customer");
        System.out.println("   ---------|--------|----------------|------------------");
        int position = 1;
        for (Token token : queue) {
            System.out.printf("   %-8d | %-6s | %-14s | %s%n",
                    position++,
                    token.getTokenNumber(),
                    token.getType(),
                    token.getCustomer().getName());
        }
    }
}

