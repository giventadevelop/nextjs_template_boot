package com.nextjstemplate.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class TicketTransactionMapperTest {

    private TicketTransactionMapper ticketTransactionMapper;

    @BeforeEach
    public void setUp() {
        ticketTransactionMapper = new TicketTransactionMapperImpl();
    }
}
