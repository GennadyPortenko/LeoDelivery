package com.cmdelivery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Created by cmdelivery on 2017-03-19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class WebSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void init() {}

    /*
    @Test
    public void testIfClientHomePageIsSecured() throws Exception {
        final ResultActions resultActions = mockMvc.perform(get("/client/home"));
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/client/login"));
    }

    @Test
    public void testIfHomePageIsSecured() throws Exception {
        final ResultActions resultActions = mockMvc.perform(get("//home"));
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/client/login"));
    }

    @Test
    @WithMockUser
    public void testIfLoggedUserHasAccessToRegularHomePage() throws Exception {
        final ResultActions resultActions = mockMvc.perform(get("/client/home"));
        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("client/home"));
    }

    @Test
    @WithMockUser(username="rest", password="rest", roles="ROLE_CONTRACTOR")
    public void testIfLoggedUserHasAccessToContractorHomePage() throws Exception {
        final ResultActions resultActions = mockMvc.perform(get("/cabinet"));
        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("contractor/cabinet"));
    }
    */

}
