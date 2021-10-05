package com.generator.password;

import com.generator.password.controller.PasswordController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PasswordGeneratorApplicationTests {

    @Autowired
    private PasswordController passwordController;

    @Test
    void contextLoads() {
        assertThat(passwordController).isNotNull();
    }

}
