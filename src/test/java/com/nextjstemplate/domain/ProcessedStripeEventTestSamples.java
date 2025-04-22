package com.nextjstemplate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProcessedStripeEventTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ProcessedStripeEvent getProcessedStripeEventSample1() {
        return new ProcessedStripeEvent().id(1L).eventId("eventId1").type("type1");
    }

    public static ProcessedStripeEvent getProcessedStripeEventSample2() {
        return new ProcessedStripeEvent().id(2L).eventId("eventId2").type("type2");
    }

    public static ProcessedStripeEvent getProcessedStripeEventRandomSampleGenerator() {
        return new ProcessedStripeEvent()
            .id(longCount.incrementAndGet())
            .eventId(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString());
    }
}
