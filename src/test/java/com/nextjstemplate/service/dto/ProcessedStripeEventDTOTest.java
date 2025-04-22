package com.nextjstemplate.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProcessedStripeEventDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcessedStripeEventDTO.class);
        ProcessedStripeEventDTO processedStripeEventDTO1 = new ProcessedStripeEventDTO();
        processedStripeEventDTO1.setId(1L);
        ProcessedStripeEventDTO processedStripeEventDTO2 = new ProcessedStripeEventDTO();
        assertThat(processedStripeEventDTO1).isNotEqualTo(processedStripeEventDTO2);
        processedStripeEventDTO2.setId(processedStripeEventDTO1.getId());
        assertThat(processedStripeEventDTO1).isEqualTo(processedStripeEventDTO2);
        processedStripeEventDTO2.setId(2L);
        assertThat(processedStripeEventDTO1).isNotEqualTo(processedStripeEventDTO2);
        processedStripeEventDTO1.setId(null);
        assertThat(processedStripeEventDTO1).isNotEqualTo(processedStripeEventDTO2);
    }
}
