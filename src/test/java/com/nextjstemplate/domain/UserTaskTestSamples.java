package com.nextjstemplate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserTaskTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserTask getUserTaskSample1() {
        return new UserTask().id(1L).title("title1").description("description1").status("status1").priority("priority1").userId("userId1");
    }

    public static UserTask getUserTaskSample2() {
        return new UserTask().id(2L).title("title2").description("description2").status("status2").priority("priority2").userId("userId2");
    }

    public static UserTask getUserTaskRandomSampleGenerator() {
        return new UserTask()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .status(UUID.randomUUID().toString())
            .priority(UUID.randomUUID().toString())
            .userId(UUID.randomUUID().toString());
    }
}
