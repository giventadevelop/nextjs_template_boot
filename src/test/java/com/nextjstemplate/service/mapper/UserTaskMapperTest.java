package com.nextjstemplate.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class UserTaskMapperTest {

    private UserTaskMapper userTaskMapper;

    @BeforeEach
    public void setUp() {
        userTaskMapper = new UserTaskMapperImpl();
    }
}
