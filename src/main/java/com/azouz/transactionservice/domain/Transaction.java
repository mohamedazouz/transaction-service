package com.azouz.transactionservice.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import javax.validation.constraints.NotNull;

/**
 * @author mazouz
 */
@JsonDeserialize(builder = Transaction.Builder.class)
public class Transaction {

  @NotNull
  private final Double amount;
  @NotNull
  private final Long timestamp;

  public Transaction(final Builder builder) {
    this.amount = builder.amount;
    this.timestamp = builder.timestamp;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Double getAmount() {
    return amount;
  }

  public Long getTimestamp() {
    return timestamp;
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) {
      return true;
    }

    if (other == null || getClass() != other.getClass()) {
      return false;
    }

    final Transaction that = (Transaction) other;

    return Objects.equal(this.getAmount(), that.getAmount())
        && Objects.equal(this.getTimestamp(), that.getTimestamp());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getAmount(), getTimestamp());
  }

  @Override
  public String toString() {
    return
        MoreObjects.toStringHelper(this)
            .add("amount", getAmount())
            .add("timestamp", getTimestamp())
            .omitNullValues().toString();
  }

  public static class Builder {

    private Double amount;
    private Long timestamp;

    public Builder withAmount(final Double amount) {
      this.amount = amount;
      return this;
    }

    public Builder withTimestamp(final Long timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    public Transaction build() {
      return new Transaction(this);
    }
  }
}
