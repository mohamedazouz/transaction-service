package com.azouz.transactionservice.config;

import com.azouz.transactionservice.rest.TransactionController;
import com.azouz.transactionservice.service.TransactionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mazouz
 */
@Configuration
public class BeanConfiguration {

  @Bean
  public TransactionService transactionService() {
    return new TransactionService();
  }

  @Bean
  public TransactionController transactionController(final TransactionService transactionService) {
    return new TransactionController(transactionService);
  }
}
