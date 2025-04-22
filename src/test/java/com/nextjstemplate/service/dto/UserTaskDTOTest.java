package com.nextjstemplate.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserTaskDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserTaskDTO.class);
        UserTaskDTO userTaskDTO1 = new UserTaskDTO();
        userTaskDTO1.setId(1L);
        UserTaskDTO userTaskDTO2 = new UserTaskDTO();
        assertThat(userTaskDTO1).isNotEqualTo(userTaskDTO2);
        userTaskDTO2.setId(userTaskDTO1.getId());
        assertThat(userTaskDTO1).isEqualTo(userTaskDTO2);
        userTaskDTO2.setId(2L);
        assertThat(userTaskDTO1).isNotEqualTo(userTaskDTO2);
        userTaskDTO1.setId(null);
        assertThat(userTaskDTO1).isNotEqualTo(userTaskDTO2);
    }
}
