package com.nextjstemplate.domain;

import static com.nextjstemplate.domain.UserTaskTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserTaskTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserTask.class);
        UserTask userTask1 = getUserTaskSample1();
        UserTask userTask2 = new UserTask();
        assertThat(userTask1).isNotEqualTo(userTask2);

        userTask2.setId(userTask1.getId());
        assertThat(userTask1).isEqualTo(userTask2);

        userTask2 = getUserTaskSample2();
        assertThat(userTask1).isNotEqualTo(userTask2);
    }
}
