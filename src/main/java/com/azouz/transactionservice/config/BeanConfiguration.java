package com.azouz.transactionservice.config;

import com.azouz.transactionservice.rest.TransactionController;
import com.azouz.transactionservice.service.TransactionService;
import com.fasterxml.jackson.databind.Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.problem.ProblemModule;

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

  @Bean
  public Module problemModule() {
    return new ProblemModule();
  }
}
