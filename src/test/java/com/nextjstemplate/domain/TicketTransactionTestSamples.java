package com.nextjstemplate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TicketTransactionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TicketTransaction getTicketTransactionSample1() {
        return new TicketTransaction()
            .id(1L)
            .email("email1")
            .ticketType("ticketType1")
            .quantity(1)
            .status("status1")
            .eventId("eventId1")
            .userId("userId1");
    }

    public static TicketTransaction getTicketTransactionSample2() {
        return new TicketTransaction()
            .id(2L)
            .email("email2")
            .ticketType("ticketType2")
            .quantity(2)
            .status("status2")
            .eventId("eventId2")
            .userId("userId2");
    }

    public static TicketTransaction getTicketTransactionRandomSampleGenerator() {
        return new TicketTransaction()
            .id(longCount.incrementAndGet())
            .email(UUID.randomUUID().toString())
            .ticketType(UUID.randomUUID().toString())
            .quantity(intCount.incrementAndGet())
            .status(UUID.randomUUID().toString())
            .eventId(UUID.randomUUID().toString())
            .userId(UUID.randomUUID().toString());
    }
}
