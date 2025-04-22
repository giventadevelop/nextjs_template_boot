package com.nextjstemplate.domain;

import static com.nextjstemplate.domain.ProcessedStripeEventTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProcessedStripeEventTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcessedStripeEvent.class);
        ProcessedStripeEvent processedStripeEvent1 = getProcessedStripeEventSample1();
        ProcessedStripeEvent processedStripeEvent2 = new ProcessedStripeEvent();
        assertThat(processedStripeEvent1).isNotEqualTo(processedStripeEvent2);

        processedStripeEvent2.setId(processedStripeEvent1.getId());
        assertThat(processedStripeEvent1).isEqualTo(processedStripeEvent2);

        processedStripeEvent2 = getProcessedStripeEventSample2();
        assertThat(processedStripeEvent1).isNotEqualTo(processedStripeEvent2);
    }
}
