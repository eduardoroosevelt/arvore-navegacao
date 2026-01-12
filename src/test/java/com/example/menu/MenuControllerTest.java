package com.example.menu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MenuControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void userMenuShouldHideUsersAndCreateUser() throws Exception {
        mockMvc.perform(get("/api/menu").with(httpBasic("user", "user123")))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("MENU.USERS"))))
                .andExpect(content().string(not(containsString("/users/new"))));
    }

    @Test
    void adminMenuShouldShowUsersAndCreateUser() throws Exception {
        mockMvc.perform(get("/api/menu").with(httpBasic("admin", "admin123")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("MENU.USERS")))
                .andExpect(content().string(containsString("/users/new"))));
    }
}
