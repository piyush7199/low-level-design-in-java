package org.lld.patterns.behavioural.chainOfResponsibility.handler.impl;

import org.lld.patterns.behavioural.chainOfResponsibility.handler.SupportHandler;
import org.lld.patterns.behavioural.chainOfResponsibility.request.Ticket;

public class Manager implements SupportHandler {
    private SupportHandler nextHandler;

    @Override
    public void handleRequest(Ticket ticket) {
        if (ticket.getSeverity() <= 5) {
            System.out.println("Junior Support handled: " + ticket.getDescription());
        } else if (nextHandler != null) {
            nextHandler.handleRequest(ticket);
        } else {
            System.out.println("No handler available for: " + ticket.getDescription());
        }
    }

    @Override
    public void setNextHandler(SupportHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
}
