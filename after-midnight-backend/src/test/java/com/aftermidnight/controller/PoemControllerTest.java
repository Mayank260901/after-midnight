package com.aftermidnight.controller;

import com.aftermidnight.entity.Poem;
import com.aftermidnight.entity.PublicationStatus;
import com.aftermidnight.entity.User;
import com.aftermidnight.security.CustomUserDetails;
import com.aftermidnight.service.PoemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PoemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PoemService poemService;

    private User testUser;
    private User otherUser;
    private CustomUserDetails customUserDetails;
    private Poem testPoem;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .build();
        otherUser = User.builder()
                .id(2L)
                .username("otheruser")
                .email("other@example.com")
                .build();
        customUserDetails = new CustomUserDetails(testUser);
        testPoem = Poem.builder()
                .id(1L)
                .title("Test Poem")
                .content("Test Content")
                .user(testUser)
                .status(PublicationStatus.PUBLISHED)
                .build();
    }

    @Test
    void createPoem_ReturnsCreatedPoem() throws Exception {
        when(poemService.create(any(Poem.class))).thenReturn(testPoem);

        mockMvc.perform(post("/api/v1/poems")
                .with(user(customUserDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPoem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Test Poem"));

        verify(poemService, times(1)).create(any(Poem.class));
    }

    @Test
    void createPoem_WithInvalidData_ReturnsBadRequest() throws Exception {
        com.aftermidnight.dto.PoemRequest invalidRequest = com.aftermidnight.dto.PoemRequest.builder()
                .title("") // Blank title
                .content("Valid content")
                .build();

        mockMvc.perform(post("/api/v1/poems")
                .with(user(customUserDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.data.title").exists());

        verify(poemService, never()).create(any(Poem.class));
    }

    @Test
    void getAllByUser_ReturnsPoemPage() throws Exception {
        Page<Poem> poemPage = new PageImpl<>(List.of(testPoem));
        when(poemService.getAllByUser(any(User.class), any(Pageable.class))).thenReturn(poemPage);

        mockMvc.perform(get("/api/v1/poems")
                .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].id").value(1))
                .andExpect(jsonPath("$.data.content[0].title").value("Test Poem"))
                .andExpect(jsonPath("$.data.totalElements").value(1));
    }

    @Test
    void getAllByUserAndStatus_ReturnsPoemPage() throws Exception {
        Page<Poem> poemPage = new PageImpl<>(List.of(testPoem));
        when(poemService.getAllByUserAndStatus(any(User.class), eq(PublicationStatus.PUBLISHED), any(Pageable.class))).thenReturn(poemPage);

        mockMvc.perform(get("/api/v1/poems")
                .param("status", "PUBLISHED")
                .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].status").value("PUBLISHED"));
    }

    @Test
    void getById_ReturnsPoem_WhenFoundAndPublished() throws Exception {
        when(poemService.getById(1L)).thenReturn(Optional.of(testPoem));

        mockMvc.perform(get("/api/v1/poems/1")
                .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void getByIdV1_ReturnsPoem_WhenFoundAndPublished() throws Exception {
        when(poemService.getById(1L)).thenReturn(Optional.of(testPoem));

        mockMvc.perform(get("/api/v1/poems/1")
                .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void getById_ReturnsForbidden_WhenDraftAndNotOwner() throws Exception {
        testPoem.setStatus(PublicationStatus.DRAFT);
        testPoem.setUser(otherUser);
        when(poemService.getById(1L)).thenReturn(Optional.of(testPoem));

        mockMvc.perform(get("/api/v1/poems/1")
                .with(user(customUserDetails)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value("UNAUTHORIZED"));
    }

    @Test
    void updateStatus_ReturnsUpdatedPoem() throws Exception {
        when(poemService.getById(1L)).thenReturn(Optional.of(testPoem));
        testPoem.setStatus(PublicationStatus.DRAFT);
        when(poemService.updateStatus(eq(1L), eq(PublicationStatus.DRAFT))).thenReturn(testPoem);

        mockMvc.perform(patch("/api/v1/poems/1/status")
                .param("status", "DRAFT")
                .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("DRAFT"));
    }

    @Test
    void deletePoem_ReturnsOk() throws Exception {
        when(poemService.getById(1L)).thenReturn(Optional.of(testPoem));
        doNothing().when(poemService).delete(1L);

        mockMvc.perform(delete("/api/v1/poems/1")
                .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Poem deleted successfully"));

        verify(poemService, times(1)).delete(1L);
    }

    @Test
    void incrementViewCount_ReturnsOk() throws Exception {
        when(poemService.getById(1L)).thenReturn(Optional.of(testPoem));
        doNothing().when(poemService).incrementViewCount(1L);

        mockMvc.perform(post("/api/v1/poems/1/view")
                .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("View count incremented"));

        verify(poemService, times(1)).incrementViewCount(1L);
    }

    @Test
    void endpoints_AreSecured() throws Exception {
        when(poemService.getAllPublished(any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));
        
        // GET is now public for poems
        mockMvc.perform(get("/api/v1/poems"))
                .andExpect(status().isOk());
        
        // POST is still secured
        mockMvc.perform(post("/api/v1/poems"))
                .andExpect(status().isForbidden());
    }
}
