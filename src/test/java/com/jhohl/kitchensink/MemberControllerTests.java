package com.jhohl.kitchensink;

import com.jhohl.kitchensink.controller.MemberController;
import com.jhohl.kitchensink.data.MemberRepository;
import com.jhohl.kitchensink.model.Member;
import com.jhohl.kitchensink.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MemberController.class)
public class MemberControllerTests {
    @Autowired
    private MockMvc mockMvc;



    @MockBean
    private MemberService memberService;  // Mock the service used by the controller

    @BeforeEach
    public void setup() {

        // Common setup can go here
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "John Doe")
                        .param("email", "john@example.com")
                        .param("phoneNumber", "1234567890"))
                .andExpect(status().is3xxRedirection())  // Assuming a redirect on success
                .andExpect(redirectedUrl("/members"));
    }

    @Test
    public void testRegisterMemberWithValidationErrors() throws Exception {
        mockMvc.perform(post("/members")
                        .param("name", "") // Assuming this should trigger a validation error
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()) // Check for redirection
                .andExpect(redirectedUrl("/members"))  // Check redirection URL
                .andDo(result -> {
                    // Optionally follow the redirect and check for error messages
                    mockMvc.perform(get("/members"))
                            .andExpect(status().isOk())
                            .andExpect(model().attributeExists("errorMessage"));
                });
    }

    @Test
    public void testRegisterUnexpectedError() throws Exception {
        doThrow(new RuntimeException("Unexpected error")).when(memberService).registerNewMember(any(Member.class));

        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "John Doe")
                        .param("email", "john@example.com")
                        .param("phoneNumber", "1234567890"))
                .andExpect(status().isInternalServerError());  // Assuming you handle exceptions to return 500
    }
}
