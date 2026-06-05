package com.bank.server.api;

import com.bank.server.BankServerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = BankServerApplication.class)
@AutoConfigureMockMvc
class TransactionControllerTest {
    @Autowired private MockMvc mockMvc;

    @Test
    void testListTransactions() throws Exception {
        mockMvc.perform(get("/api/liability/transaction")
               .param("page", "1").param("size", "10"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.code").value(200));
    }
}

