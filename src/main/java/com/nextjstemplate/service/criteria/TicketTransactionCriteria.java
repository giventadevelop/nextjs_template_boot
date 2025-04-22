package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.TicketTransaction} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.TicketTransactionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ticket-transactions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TicketTransactionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter email;

    private StringFilter ticketType;

    private IntegerFilter quantity;

    private BigDecimalFilter pricePerUnit;

    private BigDecimalFilter totalAmount;

    private StringFilter status;

    private ZonedDateTimeFilter purchaseDate;

    private StringFilter eventId;

    private StringFilter userId;

    private Boolean distinct;

    public TicketTransactionCriteria() {}

    public TicketTransactionCriteria(TicketTransactionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.ticketType = other.ticketType == null ? null : other.ticketType.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.pricePerUnit = other.pricePerUnit == null ? null : other.pricePerUnit.copy();
        this.totalAmount = other.totalAmount == null ? null : other.totalAmount.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.purchaseDate = other.purchaseDate == null ? null : other.purchaseDate.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TicketTransactionCriteria copy() {
        return new TicketTransactionCriteria(this);
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

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getTicketType() {
        return ticketType;
    }

    public StringFilter ticketType() {
        if (ticketType == null) {
            ticketType = new StringFilter();
        }
        return ticketType;
    }

    public void setTicketType(StringFilter ticketType) {
        this.ticketType = ticketType;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public IntegerFilter quantity() {
        if (quantity == null) {
            quantity = new IntegerFilter();
        }
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public BigDecimalFilter getPricePerUnit() {
        return pricePerUnit;
    }

    public BigDecimalFilter pricePerUnit() {
        if (pricePerUnit == null) {
            pricePerUnit = new BigDecimalFilter();
        }
        return pricePerUnit;
    }

    public void setPricePerUnit(BigDecimalFilter pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public BigDecimalFilter getTotalAmount() {
        return totalAmount;
    }

    public BigDecimalFilter totalAmount() {
        if (totalAmount == null) {
            totalAmount = new BigDecimalFilter();
        }
        return totalAmount;
    }

    public void setTotalAmount(BigDecimalFilter totalAmount) {
        this.totalAmount = totalAmount;
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

    public ZonedDateTimeFilter getPurchaseDate() {
        return purchaseDate;
    }

    public ZonedDateTimeFilter purchaseDate() {
        if (purchaseDate == null) {
            purchaseDate = new ZonedDateTimeFilter();
        }
        return purchaseDate;
    }

    public void setPurchaseDate(ZonedDateTimeFilter purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public StringFilter getEventId() {
        return eventId;
    }

    public StringFilter eventId() {
        if (eventId == null) {
            eventId = new StringFilter();
        }
        return eventId;
    }

    public void setEventId(StringFilter eventId) {
        this.eventId = eventId;
    }

    public StringFilter getUserId() {
        return userId;
    }

    public StringFilter userId() {
        if (userId == null) {
            userId = new StringFilter();
        }
        return userId;
    }

    public void setUserId(StringFilter userId) {
        this.userId = userId;
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
        final TicketTransactionCriteria that = (TicketTransactionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(email, that.email) &&
            Objects.equals(ticketType, that.ticketType) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(pricePerUnit, that.pricePerUnit) &&
            Objects.equals(totalAmount, that.totalAmount) &&
            Objects.equals(status, that.status) &&
            Objects.equals(purchaseDate, that.purchaseDate) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, ticketType, quantity, pricePerUnit, totalAmount, status, purchaseDate, eventId, userId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TicketTransactionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (ticketType != null ? "ticketType=" + ticketType + ", " : "") +
            (quantity != null ? "quantity=" + quantity + ", " : "") +
            (pricePerUnit != null ? "pricePerUnit=" + pricePerUnit + ", " : "") +
            (totalAmount != null ? "totalAmount=" + totalAmount + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (purchaseDate != null ? "purchaseDate=" + purchaseDate + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
