package com.azouz.transactionservice.rest;

import com.azouz.transactionservice.domain.Transaction;
import com.azouz.transactionservice.domain.TransactionStats;
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
@RequestMapping(TransactionController.NAMESPACE)
public class TransactionController {
    public static final String NAMESPACE = "transaction";

    private static final Logger LOG = LoggerFactory.getLogger(TransactionController.class);

    final TransactionService transactionService;

    public TransactionController(final TransactionService transactionService) {
        this.transactionService = transactionService;

    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> createTransaction(@RequestBody @Valid final Transaction transaction) {
        transactionService.insertTransaction(transaction);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public TransactionStats getTransactions() {
        return transactionService.getTransactionStats();
    }
}
