package com.azouz.transactionservice.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * @author mazouz
 */
@JsonDeserialize(builder = TransactionStats.Builder.class)
public class TransactionStats {

  private final double sum;
  private final double avg;
  private final double max;
  private final double min;
  private final long count;

  public TransactionStats(final Builder builder) {
    this.sum = builder.sum;
    this.avg = builder.avg;
    this.max = builder.max;
    this.min = builder.min;
    this.count = builder.count;
  }

  public static Builder builder() {
    return new Builder();
  }

  public double getSum() {
    return sum;
  }

  public double getAvg() {
    return avg;
  }

  public double getMax() {
    return max;
  }

  public double getMin() {
    return min;
  }

  public long getCount() {
    return count;
  }

  @Override
  public String toString() {
    return
        MoreObjects.toStringHelper(this)
            .add("sum", getSum())
            .add("avg", getAvg())
            .add("min", getMin())
            .add("max", getMax())
            .add("count", getCount())
            .omitNullValues().toString();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(sum, avg, max, min, count);
  }

  public static class Builder {

    private double sum;
    private double avg;
    private double max;
    private double min;
    private long count;


    public Builder withSum(final double sum) {
      this.sum = sum;
      return this;
    }

    public Builder withAvg(final double avg) {
      this.avg = avg;
      return this;
    }

    public Builder withMax(final double max) {
      this.max = max;
      return this;
    }

    public Builder withMin(final double min) {
      this.min = min;
      return this;
    }

    public Builder withCount(final long count) {
      this.count = count;
      return this;
    }

    public TransactionStats build() {
      return new TransactionStats(this);
    }
  }
}
