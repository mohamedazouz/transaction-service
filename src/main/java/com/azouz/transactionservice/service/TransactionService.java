package com.azouz.transactionservice.service;

import com.azouz.transactionservice.domain.Transaction;
import com.azouz.transactionservice.domain.TransactionStats;
import com.azouz.transactionservice.exception.NotValidTimestampException;
import com.google.common.collect.Lists;
import java.text.MessageFormat;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * @author mazouz
 */
public class TransactionService {

  public static final int MAX_MILLISECONDS = 60000;
  private final ConcurrentHashMap<Long, List<Double>> transactionTimeMap;

  public TransactionService() {
    transactionTimeMap = new ConcurrentHashMap<>();
  }

  public void insertTransaction(final Transaction transaction) throws NotValidTimestampException {
    long timestamp = transaction.getTimestamp();
    if (!isValidTimestamp(timestamp)) {
      throw new NotValidTimestampException(
          MessageFormat.format("not valid timestamp: {0}", timestamp));
    }
    List<Double> transactions = transactionTimeMap.get(timestamp);
    if (transactions == null) {
      transactions = Lists.newArrayList();
    }
    transactions.add(transaction.getAmount());
    transactionTimeMap.put(transaction.getTimestamp(), transactions);
  }

  private boolean isValidTimestamp(final Long timestamp) {
    long timeDiff = getCurrentTimestamp() - timestamp;
    if (timeDiff > MAX_MILLISECONDS) {
      return false;
    }
    return true;
  }


  public TransactionStats getTransactionStats() {
    final long currentTimestamp = getCurrentTimestamp();
    final Long last60Seconds = currentTimestamp - MAX_MILLISECONDS;
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
