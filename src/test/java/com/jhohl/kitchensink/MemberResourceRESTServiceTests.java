package com.jhohl.kitchensink;

import com.jhohl.kitchensink.data.MemberRepository;
import com.jhohl.kitchensink.model.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberResourceRESTServiceTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setup() {
        memberRepository.deleteAll();
        memberRepository.save(new Member("Alice", "alice@example.com", "1234567890"));
        memberRepository.save(new Member("Bob", "bob@example.com", "0987654321"));
    }
    @Test
    public void shouldListAllMembers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))) // Assuming there are 2 members for the test scenario
                .andDo(print());
    }

    @Test
    public void shouldCreateMember() throws Exception {
        String memberJson = "{\"name\":\"John Doe\",\"email\":\"justin@example.com\",\"phoneNumber\":\"1234567899\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/rest/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(memberJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andDo(print());
    }

    @Test
    public void shouldReturnNotFoundWhenMemberDoesNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/members/999"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void shouldHandleInvalidMemberData() throws Exception {
        String invalidMemberJson = "{\"name\":\"\",\"email\":\"notanemail\",\"phoneNumber\":\"123\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/rest/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidMemberJson))
                .andExpect(status().isBadRequest()) // Check for validation response
                .andDo(print());
    }

    @Test
    public void shouldDeleteMember() throws Exception {
        // Assume there's a member with ID 1
        mockMvc.perform(MockMvcRequestBuilders.delete("/rest/members/1"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void shouldUpdateMember() throws Exception {
        String memberJson = "{\"name\":\"Updated Name\",\"email\":\"update@example.com\",\"phoneNumber\":\"9876543210\"}";
        mockMvc.perform(MockMvcRequestBuilders.put("/rest/members/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(memberJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("update@example.com"))
                .andDo(print());
    }
}
