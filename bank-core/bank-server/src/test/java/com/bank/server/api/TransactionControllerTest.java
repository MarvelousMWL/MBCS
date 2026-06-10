package com.bank.server.api;

import com.bank.server.BankServerApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = BankServerApplication.class)
@AutoConfigureMockMvc
class TransactionControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    private String authToken;

    @BeforeEach
    void login() throws Exception {
        String loginJson = "{\"tellerNo\":\"1000010001\",\"password\":\"123456\",\"institutionNo\":\"100001\",\"force\":true}";
        var result = mockMvc.perform(post("/api/teller/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk())
                .andReturn();
        var json = objectMapper.readTree(result.getResponse().getContentAsString());
        authToken = json.get("data").get("token").asText();
    }

    @Test
    void testListTransactions() throws Exception {
        mockMvc.perform(get("/api/liability/transaction")
               .param("page", "1").param("size", "10")
               .header("Authorization", authToken))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.code").value(200));
    }
}
