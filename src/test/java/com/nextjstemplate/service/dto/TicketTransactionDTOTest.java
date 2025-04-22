package com.nextjstemplate.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TicketTransactionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TicketTransactionDTO.class);
        TicketTransactionDTO ticketTransactionDTO1 = new TicketTransactionDTO();
        ticketTransactionDTO1.setId(1L);
        TicketTransactionDTO ticketTransactionDTO2 = new TicketTransactionDTO();
        assertThat(ticketTransactionDTO1).isNotEqualTo(ticketTransactionDTO2);
        ticketTransactionDTO2.setId(ticketTransactionDTO1.getId());
        assertThat(ticketTransactionDTO1).isEqualTo(ticketTransactionDTO2);
        ticketTransactionDTO2.setId(2L);
        assertThat(ticketTransactionDTO1).isNotEqualTo(ticketTransactionDTO2);
        ticketTransactionDTO1.setId(null);
        assertThat(ticketTransactionDTO1).isNotEqualTo(ticketTransactionDTO2);
    }
}
