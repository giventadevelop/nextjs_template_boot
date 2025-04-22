package com.nextjstemplate.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class ProcessedStripeEventMapperTest {

    private ProcessedStripeEventMapper processedStripeEventMapper;

    @BeforeEach
    public void setUp() {
        processedStripeEventMapper = new ProcessedStripeEventMapperImpl();
    }
}
