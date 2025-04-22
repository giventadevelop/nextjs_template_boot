package com.nextjstemplate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserSubscriptionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserSubscription getUserSubscriptionSample1() {
        return new UserSubscription()
            .id(1L)
            .stripeCustomerId("stripeCustomerId1")
            .stripeSubscriptionId("stripeSubscriptionId1")
            .stripePriceId("stripePriceId1")
            .status("status1");
    }

    public static UserSubscription getUserSubscriptionSample2() {
        return new UserSubscription()
            .id(2L)
            .stripeCustomerId("stripeCustomerId2")
            .stripeSubscriptionId("stripeSubscriptionId2")
            .stripePriceId("stripePriceId2")
            .status("status2");
    }

    public static UserSubscription getUserSubscriptionRandomSampleGenerator() {
        return new UserSubscription()
            .id(longCount.incrementAndGet())
            .stripeCustomerId(UUID.randomUUID().toString())
            .stripeSubscriptionId(UUID.randomUUID().toString())
            .stripePriceId(UUID.randomUUID().toString())
            .status(UUID.randomUUID().toString());
    }
}
