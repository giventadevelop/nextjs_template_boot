package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.ProcessedStripeEvent} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.ProcessedStripeEventResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /processed-stripe-events?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProcessedStripeEventCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter eventId;

    private StringFilter type;

    private ZonedDateTimeFilter processedAt;

    private Boolean distinct;

    public ProcessedStripeEventCriteria() {}

    public ProcessedStripeEventCriteria(ProcessedStripeEventCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.processedAt = other.processedAt == null ? null : other.processedAt.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProcessedStripeEventCriteria copy() {
        return new ProcessedStripeEventCriteria(this);
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

    public StringFilter getType() {
        return type;
    }

    public StringFilter type() {
        if (type == null) {
            type = new StringFilter();
        }
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public ZonedDateTimeFilter getProcessedAt() {
        return processedAt;
    }

    public ZonedDateTimeFilter processedAt() {
        if (processedAt == null) {
            processedAt = new ZonedDateTimeFilter();
        }
        return processedAt;
    }

    public void setProcessedAt(ZonedDateTimeFilter processedAt) {
        this.processedAt = processedAt;
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
        final ProcessedStripeEventCriteria that = (ProcessedStripeEventCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(type, that.type) &&
            Objects.equals(processedAt, that.processedAt) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventId, type, processedAt, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcessedStripeEventCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (processedAt != null ? "processedAt=" + processedAt + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
