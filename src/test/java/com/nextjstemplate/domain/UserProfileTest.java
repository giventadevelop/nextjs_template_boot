package com.nextjstemplate.domain;

import static com.nextjstemplate.domain.UserProfileTestSamples.*;
import static com.nextjstemplate.domain.UserSubscriptionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProfile.class);
        UserProfile userProfile1 = getUserProfileSample1();
        UserProfile userProfile2 = new UserProfile();
        assertThat(userProfile1).isNotEqualTo(userProfile2);

        userProfile2.setId(userProfile1.getId());
        assertThat(userProfile1).isEqualTo(userProfile2);

        userProfile2 = getUserProfileSample2();
        assertThat(userProfile1).isNotEqualTo(userProfile2);
    }

    @Test
    void userSubscriptionTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserSubscription userSubscriptionBack = getUserSubscriptionRandomSampleGenerator();

        userProfile.setUserSubscription(userSubscriptionBack);
        assertThat(userProfile.getUserSubscription()).isEqualTo(userSubscriptionBack);
        assertThat(userSubscriptionBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.userSubscription(null);
        assertThat(userProfile.getUserSubscription()).isNull();
        assertThat(userSubscriptionBack.getUserProfile()).isNull();
    }
}
