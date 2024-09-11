package com.jhohl.kitchensink;

import com.jhohl.kitchensink.controller.MemberController;
import com.jhohl.kitchensink.data.MemberRepository;
import com.jhohl.kitchensink.model.Member;
import com.jhohl.kitchensink.service.MemberService;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ActiveProfiles("test")
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
    public void testRegisterMemberSuccess() throws Exception {
        Member member = new Member();
        member.setName("John");
        member.setEmail("john@example.com");
        member.setPhoneNumber("1234567890");

        when(memberService.getMembers()).thenReturn(Collections.singletonList(member));

        mockMvc.perform(post("/")
                        .flashAttr("newMember", member))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("successMessage"))
                .andExpect(flash().attribute("successMessage", "Member registered successfully!"));
    }
    @Test
    public void testRegisterMemberValidationError() throws Exception {
        Member member = new Member();
        member.setEmail("john@example.com");
        member.setPhoneNumber("1234567890");

        mockMvc.perform(post("/")
                        .flashAttr("newMember", member))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("newMember", "name"))
                .andExpect(view().name("home"));
    }

    @Test
    public void testRegisterMemberDuplicateEmail() throws Exception {
        Member member = new Member();
        member.setName("John");
        member.setEmail("john@example.com");
        member.setPhoneNumber("1234567890");

        doThrow(new ValidationException("A user with this email already exists.")).when(memberService).registerNewMember(any(Member.class));

        mockMvc.perform(post("/")
                        .flashAttr("newMember", member))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andExpect(flash().attribute("errorMessage", "A user with this email already exists."));
    }

    @Test
    public void testHomePageWithNoMembers() throws Exception {
        when(memberService.getMembers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("members", Collections.emptyList()))
                .andExpect(view().name("home"));
    }
}
