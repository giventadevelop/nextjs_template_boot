package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.UserSubscription} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserSubscriptionDTO implements Serializable {

    private Long id;

    private String stripeCustomerId;

    private String stripeSubscriptionId;

    private String stripePriceId;

    private ZonedDateTime stripeCurrentPeriodEnd;

    @NotNull
    private String status;

    private UserProfileDTO userProfile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public String getStripeSubscriptionId() {
        return stripeSubscriptionId;
    }

    public void setStripeSubscriptionId(String stripeSubscriptionId) {
        this.stripeSubscriptionId = stripeSubscriptionId;
    }

    public String getStripePriceId() {
        return stripePriceId;
    }

    public void setStripePriceId(String stripePriceId) {
        this.stripePriceId = stripePriceId;
    }

    public ZonedDateTime getStripeCurrentPeriodEnd() {
        return stripeCurrentPeriodEnd;
    }

    public void setStripeCurrentPeriodEnd(ZonedDateTime stripeCurrentPeriodEnd) {
        this.stripeCurrentPeriodEnd = stripeCurrentPeriodEnd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserProfileDTO getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfileDTO userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserSubscriptionDTO)) {
            return false;
        }

        UserSubscriptionDTO userSubscriptionDTO = (UserSubscriptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userSubscriptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserSubscriptionDTO{" +
            "id=" + getId() +
            ", stripeCustomerId='" + getStripeCustomerId() + "'" +
            ", stripeSubscriptionId='" + getStripeSubscriptionId() + "'" +
            ", stripePriceId='" + getStripePriceId() + "'" +
            ", stripeCurrentPeriodEnd='" + getStripeCurrentPeriodEnd() + "'" +
            ", status='" + getStatus() + "'" +
            ", userProfile=" + getUserProfile() +
            "}";
    }
}
