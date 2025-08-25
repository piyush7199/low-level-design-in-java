package org.lld.patterns.behavioural.chainOfResponsibility.handler;

import org.lld.patterns.behavioural.chainOfResponsibility.request.Ticket;

public interface SupportHandler {
    void handleRequest(Ticket ticket);
    void setNextHandler(SupportHandler nextHandler);
}
