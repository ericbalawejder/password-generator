package com.generator.password.controller;

import com.generator.password.exception.PasswordException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(PasswordController.class)
class PasswordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void generatePassword() throws Exception {
        mockMvc.perform(get("/password"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-policy"));
    }

    @Test
    void testShowPasswordIsSuccessful() throws Exception {
        mockMvc.perform(post("/show")
                        .param("length", "16")
                        .param("uppercase", "2")
                        .param("digit", "2")
                        .param("specialChar", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenPasswordTooShortArgument_whenGetSpecificException_thenBadRequest() throws Exception {
        mockMvc.perform(post("/show")
                        .param("length", "7")
                        .param("uppercase", "2")
                        .param("digit", "2")
                        .param("specialChar", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(response -> assertTrue(
                        response.getResolvedException() instanceof PasswordException))
                .andExpect(response -> assertEquals("password length must be 8 - 128 characters",
                        Objects.requireNonNull(response.getResolvedException()).getMessage()));
    }

    @Test
    public void givenNegativeArgumentValue_whenGetSpecificException_thenBadRequest() throws Exception {
        mockMvc.perform(post("/show")
                        .param("length", "32")
                        .param("uppercase", "-1")
                        .param("digit", "2")
                        .param("specialChar", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(response -> assertTrue(
                        response.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(response -> assertEquals("invalid field size",
                        Objects.requireNonNull(response.getResolvedException()).getMessage()));
    }

    @Test
    public void givenArgumentsGreaterThanPasswordLength_whenGetSpecificException_thenBadRequest()
            throws Exception {
        mockMvc.perform(post("/show")
                        .param("length", "32")
                        .param("uppercase", "2")
                        .param("digit", "29")
                        .param("specialChar", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(response -> assertTrue(
                        response.getResolvedException() instanceof PasswordException))
                .andExpect(response -> assertEquals("requirement fields exceed password length",
                        Objects.requireNonNull(response.getResolvedException()).getMessage()));
    }

}
