package com.azouz.transactionservice.rest;

import com.azouz.transactionservice.domain.Transaction;
import com.azouz.transactionservice.domain.TransactionStats;
import com.azouz.transactionservice.exception.NotValidTimestampException;
import com.azouz.transactionservice.service.TransactionService;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author mazouz
 */
@RestController
public class TransactionController {

  public static final String TRANSACTION_NAMESPACE = "/transactions";
  public static final String STATISTICS_NAMESPACE = "/statistics";

  private static final Logger LOG = LoggerFactory.getLogger(TransactionController.class);

  final TransactionService transactionService;

  public TransactionController(final TransactionService transactionService) {
    this.transactionService = transactionService;

  }

  @RequestMapping(path = TRANSACTION_NAMESPACE, method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity<Void> createTransaction(@RequestBody @Valid final Transaction transaction)
      throws NotValidTimestampException {
    transactionService.insertTransaction(transaction);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }


  @RequestMapping(path = STATISTICS_NAMESPACE, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public TransactionStats getTransactions() {
    return transactionService.getTransactionStats();
  }
}
