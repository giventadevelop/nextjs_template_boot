package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.ProcessedStripeEvent} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProcessedStripeEventDTO implements Serializable {

    private Long id;

    @NotNull
    private String eventId;

    @NotNull
    private String type;

    @NotNull
    private ZonedDateTime processedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ZonedDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(ZonedDateTime processedAt) {
        this.processedAt = processedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcessedStripeEventDTO)) {
            return false;
        }

        ProcessedStripeEventDTO processedStripeEventDTO = (ProcessedStripeEventDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, processedStripeEventDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcessedStripeEventDTO{" +
            "id=" + getId() +
            ", eventId='" + getEventId() + "'" +
            ", type='" + getType() + "'" +
            ", processedAt='" + getProcessedAt() + "'" +
            "}";
    }
}
