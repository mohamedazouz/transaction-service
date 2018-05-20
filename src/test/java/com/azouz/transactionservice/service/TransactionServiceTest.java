package com.azouz.transactionservice.service;

import static com.azouz.transactionservice.DateUtils.getCurrentTimestamp;
import static org.junit.Assert.assertEquals;

import com.azouz.transactionservice.domain.Transaction;
import com.azouz.transactionservice.domain.TransactionStats;
import com.azouz.transactionservice.exception.NotValidTimestampException;
import org.junit.Before;
import org.junit.Test;

public class TransactionServiceTest {

  TransactionService transactionService;

  @Before
  public void setup() {
    transactionService = new TransactionService();
  }

  @Test
  public void addNewTransactionExpectNoException() throws NotValidTimestampException {
    final long currentTimestamp = getCurrentTimestamp();
    final Transaction transaction = Transaction.builder()
        .withAmount(1234.0)
        .withTimestamp(currentTimestamp).build();
    transactionService.insertTransaction(transaction);
  }

  @Test(expected = NotValidTimestampException.class)
  public void addNewTransactionWithInvalidTimestampExpectException()
      throws NotValidTimestampException {
    final long currentTimestamp = 1526814821834L;
    final Transaction transaction = Transaction.builder()
        .withAmount(1234.0)
        .withTimestamp(currentTimestamp).build();
    transactionService.insertTransaction(transaction);
  }

  @Test
  public void AddTransactionThengetStats()
      throws NotValidTimestampException {
    final long currentTimestamp = getCurrentTimestamp();
    final Transaction transaction = Transaction.builder()
        .withAmount(3.0)
        .withTimestamp(currentTimestamp).build();
    transactionService.insertTransaction(transaction);
    final TransactionStats expectedStats = TransactionStats.builder()
        .withAvg(3.0).withCount(1).withSum(3).withMax(3).withMin(3).build();
    final TransactionStats actualStats = transactionService.getTransactionStats();
    assertEquals(expectedStats, actualStats);
  }

  @Test
  public void AddMoreTransactionsThenGetStats()
      throws NotValidTimestampException {
    final Transaction.Builder transactionBuilder = Transaction.builder()
        .withAmount(3.0)
        .withTimestamp(getCurrentTimestamp());
    transactionService.insertTransaction(transactionBuilder.build());
    transactionService.insertTransaction(transactionBuilder.withAmount(4.0).build());
    transactionService.insertTransaction(
        transactionBuilder.withAmount(5.0).withTimestamp(getCurrentTimestamp()).build());

    final TransactionStats expectedStats = TransactionStats.builder()
        .withAvg(4.0).withCount(3).withSum(12).withMax(5).withMin(3).build();
    final TransactionStats actualStats = transactionService.getTransactionStats();
    assertEquals(expectedStats, actualStats);
  }


  @Test
  public void AddMoreTransactionsThenWait10SecondsThenGetStats()
      throws NotValidTimestampException, InterruptedException {
    final Transaction.Builder transactionBuilder = Transaction.builder()
        .withAmount(3.0)
        .withTimestamp(getCurrentTimestamp() - 55000);
    transactionService.insertTransaction(transactionBuilder.build());
    transactionService.insertTransaction(transactionBuilder.withAmount(4.0).build());
    transactionService.insertTransaction(
        transactionBuilder.withAmount(5.0).withTimestamp(getCurrentTimestamp() - 55000).build());

    final TransactionStats expectedStats = TransactionStats.builder()
        .withAvg(0).withCount(0).withSum(0).withMax(0).withMin(0).build();

    Thread.sleep(10000);
    final TransactionStats actualStats = transactionService.getTransactionStats();
    assertEquals(expectedStats, actualStats);
  }


}
