package com.azouz.transactionservice.service;

import com.azouz.transactionservice.domain.Transaction;
import com.azouz.transactionservice.domain.TransactionStats;
import com.google.common.collect.Lists;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.Seconds;

/**
 * @author mazouz
 */
public class TransactionService {

  private final ConcurrentHashMap<Long, List<Double>> transactionTimeMap;

  public TransactionService() {
    transactionTimeMap = new ConcurrentHashMap<>();
  }

  public void insertTransaction(final Transaction transaction) {
    List<Double> transactions = transactionTimeMap.get(transaction.getTimestamp());
    if (transactions == null) {
      transactions = Lists.newArrayList();
    }
    transactions.add(transaction.getAmount());
    transactionTimeMap.put(transaction.getTimestamp(), transactions);
  }


  public TransactionStats getTransactionStats() {
    final long currentTimestamp = getCurrentTimestamp();
    final Long last60Seconds = currentTimestamp - 60000;
    final Stream transActionsStreem = LongStream
        .rangeClosed(last60Seconds, currentTimestamp)
        .mapToObj(index -> transactionTimeMap.get(index))
        .filter(Objects::nonNull)
        .flatMap(amount -> amount.stream());

    final DoubleSummaryStatistics stats = transActionsStreem
        .mapToDouble(amount -> (Double) amount).summaryStatistics();
    final TransactionStats.Builder builder = TransactionStats.builder();
    if (stats.getCount() == 0) {
      return builder.build();
    }
    return builder.withAvg(stats.getAverage())
        .withCount(stats.getCount())
        .withSum(stats.getSum())
        .withMin(stats.getMin())
        .withMax(stats.getMax())
        .build();
  }

  private static long getCurrentTimestamp() {
    return DateTime.now(DateTimeZone.UTC).getMillis();
  }
}
