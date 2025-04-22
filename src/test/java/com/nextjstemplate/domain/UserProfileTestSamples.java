package com.nextjstemplate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserProfileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserProfile getUserProfileSample1() {
        return new UserProfile()
            .id(1L)
            .userId("userId1")
            .firstName("firstName1")
            .lastName("lastName1")
            .email("email1")
            .phone("phone1")
            .addressLine1("addressLine11")
            .addressLine2("addressLine21")
            .city("city1")
            .state("state1")
            .zipCode("zipCode1")
            .country("country1")
            .notes("notes1");
    }

    public static UserProfile getUserProfileSample2() {
        return new UserProfile()
            .id(2L)
            .userId("userId2")
            .firstName("firstName2")
            .lastName("lastName2")
            .email("email2")
            .phone("phone2")
            .addressLine1("addressLine12")
            .addressLine2("addressLine22")
            .city("city2")
            .state("state2")
            .zipCode("zipCode2")
            .country("country2")
            .notes("notes2");
    }

    public static UserProfile getUserProfileRandomSampleGenerator() {
        return new UserProfile()
            .id(longCount.incrementAndGet())
            .userId(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .addressLine1(UUID.randomUUID().toString())
            .addressLine2(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .state(UUID.randomUUID().toString())
            .zipCode(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString())
            .notes(UUID.randomUUID().toString());
    }
}
