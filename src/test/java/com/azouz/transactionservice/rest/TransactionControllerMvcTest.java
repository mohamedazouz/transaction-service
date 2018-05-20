package com.azouz.transactionservice.rest;

import static com.azouz.transactionservice.DateUtils.getCurrentTimestamp;
import static com.google.common.base.Charsets.UTF_8;
import static org.junit.Assert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.azouz.transactionservice.domain.Transaction;
import com.azouz.transactionservice.domain.TransactionStats;
import com.azouz.transactionservice.exception.ControllerAdviceTraits;
import com.azouz.transactionservice.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * @author mazouz
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TransactionControllerMvcTest.ApplicationTestContext.class})
@WebAppConfiguration
public class TransactionControllerMvcTest {

  @Autowired
  private TransactionController transactionController;

  private MockMvc mockMvc;
  private static final MediaType APPLICATION_JSON_WITH_UTF8 = new MediaType("application", "json",
      UTF_8);

  @Before
  public void setup() {
    this.mockMvc =
        MockMvcBuilders.standaloneSetup(transactionController)
            .setControllerAdvice(new ControllerAdviceTraits())
            .build();
  }

  @Test
  public void addNewTransactionExpectNoException() throws Exception {
    final Transaction.Builder transactionBuilder = Transaction.builder()
        .withAmount(3.0)
        .withTimestamp(getCurrentTimestamp());
    final ObjectMapper mapper = new ObjectMapper();
    mockMvc.perform(post(TransactionController.NAMESPACE)
        .contentType(APPLICATION_JSON)
        .content(mapper.writeValueAsString(transactionBuilder.build())))
        .andExpect(status().isCreated());
  }

  @Test
  public void AddTransactionThengetStats() throws Exception {
    final ObjectMapper mapper = new ObjectMapper();
    final TransactionStats expectedStats = TransactionStats.builder()
        .withAvg(3.0).withCount(1).withSum(3).withMax(3).withMin(3).build();
    mockMvc.perform(get(TransactionController.NAMESPACE)
        .contentType(APPLICATION_JSON))
        .andExpect(result -> {
          final String json = result.getResponse().getContentAsString();
          final TransactionStats actualStats = mapper.readValue(json, TransactionStats.class);
          assertEquals(expectedStats, actualStats);
        });
  }

  @Configuration
  static class ApplicationTestContext {

    @Bean
    public TransactionService transactionService() {
      return new TransactionService();
    }

    @Bean
    public TransactionController transactionController(
        final TransactionService transactionService) {
      return new TransactionController(transactionService);
    }
  }
}
