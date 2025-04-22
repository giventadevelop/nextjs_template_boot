package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.TicketTransaction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TicketTransactionDTO implements Serializable {

    private Long id;

    @NotNull
    private String email;

    @NotNull
    private String ticketType;

    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal pricePerUnit;

    @NotNull
    private BigDecimal totalAmount;

    @NotNull
    private String status;

    @NotNull
    private ZonedDateTime purchaseDate;

    @NotNull
    private String eventId;

    private String userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ZonedDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(ZonedDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TicketTransactionDTO)) {
            return false;
        }

        TicketTransactionDTO ticketTransactionDTO = (TicketTransactionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ticketTransactionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TicketTransactionDTO{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", ticketType='" + getTicketType() + "'" +
            ", quantity=" + getQuantity() +
            ", pricePerUnit=" + getPricePerUnit() +
            ", totalAmount=" + getTotalAmount() +
            ", status='" + getStatus() + "'" +
            ", purchaseDate='" + getPurchaseDate() + "'" +
            ", eventId='" + getEventId() + "'" +
            ", userId='" + getUserId() + "'" +
            "}";
    }
}
