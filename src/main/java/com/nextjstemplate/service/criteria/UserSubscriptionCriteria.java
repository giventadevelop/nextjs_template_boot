package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.UserSubscription} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.UserSubscriptionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-subscriptions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserSubscriptionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter stripeCustomerId;

    private StringFilter stripeSubscriptionId;

    private StringFilter stripePriceId;

    private ZonedDateTimeFilter stripeCurrentPeriodEnd;

    private StringFilter status;

    private LongFilter userProfileId;

    private Boolean distinct;

    public UserSubscriptionCriteria() {}

    public UserSubscriptionCriteria(UserSubscriptionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.stripeCustomerId = other.stripeCustomerId == null ? null : other.stripeCustomerId.copy();
        this.stripeSubscriptionId = other.stripeSubscriptionId == null ? null : other.stripeSubscriptionId.copy();
        this.stripePriceId = other.stripePriceId == null ? null : other.stripePriceId.copy();
        this.stripeCurrentPeriodEnd = other.stripeCurrentPeriodEnd == null ? null : other.stripeCurrentPeriodEnd.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.userProfileId = other.userProfileId == null ? null : other.userProfileId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public UserSubscriptionCriteria copy() {
        return new UserSubscriptionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getStripeCustomerId() {
        return stripeCustomerId;
    }

    public StringFilter stripeCustomerId() {
        if (stripeCustomerId == null) {
            stripeCustomerId = new StringFilter();
        }
        return stripeCustomerId;
    }

    public void setStripeCustomerId(StringFilter stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public StringFilter getStripeSubscriptionId() {
        return stripeSubscriptionId;
    }

    public StringFilter stripeSubscriptionId() {
        if (stripeSubscriptionId == null) {
            stripeSubscriptionId = new StringFilter();
        }
        return stripeSubscriptionId;
    }

    public void setStripeSubscriptionId(StringFilter stripeSubscriptionId) {
        this.stripeSubscriptionId = stripeSubscriptionId;
    }

    public StringFilter getStripePriceId() {
        return stripePriceId;
    }

    public StringFilter stripePriceId() {
        if (stripePriceId == null) {
            stripePriceId = new StringFilter();
        }
        return stripePriceId;
    }

    public void setStripePriceId(StringFilter stripePriceId) {
        this.stripePriceId = stripePriceId;
    }

    public ZonedDateTimeFilter getStripeCurrentPeriodEnd() {
        return stripeCurrentPeriodEnd;
    }

    public ZonedDateTimeFilter stripeCurrentPeriodEnd() {
        if (stripeCurrentPeriodEnd == null) {
            stripeCurrentPeriodEnd = new ZonedDateTimeFilter();
        }
        return stripeCurrentPeriodEnd;
    }

    public void setStripeCurrentPeriodEnd(ZonedDateTimeFilter stripeCurrentPeriodEnd) {
        this.stripeCurrentPeriodEnd = stripeCurrentPeriodEnd;
    }

    public StringFilter getStatus() {
        return status;
    }

    public StringFilter status() {
        if (status == null) {
            status = new StringFilter();
        }
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public LongFilter getUserProfileId() {
        return userProfileId;
    }

    public LongFilter userProfileId() {
        if (userProfileId == null) {
            userProfileId = new LongFilter();
        }
        return userProfileId;
    }

    public void setUserProfileId(LongFilter userProfileId) {
        this.userProfileId = userProfileId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserSubscriptionCriteria that = (UserSubscriptionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(stripeCustomerId, that.stripeCustomerId) &&
            Objects.equals(stripeSubscriptionId, that.stripeSubscriptionId) &&
            Objects.equals(stripePriceId, that.stripePriceId) &&
            Objects.equals(stripeCurrentPeriodEnd, that.stripeCurrentPeriodEnd) &&
            Objects.equals(status, that.status) &&
            Objects.equals(userProfileId, that.userProfileId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            stripeCustomerId,
            stripeSubscriptionId,
            stripePriceId,
            stripeCurrentPeriodEnd,
            status,
            userProfileId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserSubscriptionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (stripeCustomerId != null ? "stripeCustomerId=" + stripeCustomerId + ", " : "") +
            (stripeSubscriptionId != null ? "stripeSubscriptionId=" + stripeSubscriptionId + ", " : "") +
            (stripePriceId != null ? "stripePriceId=" + stripePriceId + ", " : "") +
            (stripeCurrentPeriodEnd != null ? "stripeCurrentPeriodEnd=" + stripeCurrentPeriodEnd + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (userProfileId != null ? "userProfileId=" + userProfileId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
