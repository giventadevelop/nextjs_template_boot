package com.nextjstemplate.domain;

import static com.nextjstemplate.domain.TicketTransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TicketTransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TicketTransaction.class);
        TicketTransaction ticketTransaction1 = getTicketTransactionSample1();
        TicketTransaction ticketTransaction2 = new TicketTransaction();
        assertThat(ticketTransaction1).isNotEqualTo(ticketTransaction2);

        ticketTransaction2.setId(ticketTransaction1.getId());
        assertThat(ticketTransaction1).isEqualTo(ticketTransaction2);

        ticketTransaction2 = getTicketTransactionSample2();
        assertThat(ticketTransaction1).isNotEqualTo(ticketTransaction2);
    }
}
