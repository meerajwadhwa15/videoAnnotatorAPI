package com.videoannotator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.videoannotator.model.request.RegisterRequest;
import com.videoannotator.model.response.ErrorResponse;
import com.videoannotator.service.IUserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private IUserService userService;

    @Test
    void whenValidInput_thenReturn200() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFullName("Test").setEmail("test@test.com").setPassword("12345").setMatchingPassword("12345");
        mockMvc.perform(post("/user/signup")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void whenNullInput_thenReturn400() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFullName("Test").setEmail(null).setPassword("12345").setMatchingPassword("12345");
        mockMvc.perform(post("/user/signup")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenValidInput_thenCallToService() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFullName("Test").setEmail("test@test.com").setPassword("12345").setMatchingPassword("12345");
        mockMvc.perform(post("/user/signup")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        //Check request send to service is matching
        ArgumentCaptor<RegisterRequest> userCaptor = ArgumentCaptor.forClass(RegisterRequest.class);
        verify(userService, times(1)).registerUser(userCaptor.capture());
        assertThat(userCaptor.getValue().getFullName()).isEqualTo(registerRequest.getFullName());
        assertThat(userCaptor.getValue().getEmail()).isEqualTo(registerRequest.getEmail());
    }

    @Test
    void whenNullInput_thenReturn400AndErrorMessage() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFullName("Test").setEmail(null).setPassword("12345").setMatchingPassword("12345");
        MvcResult mvcResult = mockMvc.perform(post("/user/signup")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();
        ErrorResponse expectedErrorResponse = new ErrorResponse().setCode("400").setMessage("Invalid data input").setError(Collections.singletonList("email must not be blank"));
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(expectedErrorResponse);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }
}
