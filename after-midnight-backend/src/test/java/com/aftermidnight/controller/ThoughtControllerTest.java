package com.aftermidnight.controller;

import com.aftermidnight.entity.Thought;
import com.aftermidnight.entity.PublicationStatus;
import com.aftermidnight.entity.User;
import com.aftermidnight.security.CustomUserDetails;
import com.aftermidnight.service.ThoughtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ThoughtControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ThoughtService thoughtService;

    private User testUser;
    private User otherUser;
    private CustomUserDetails customUserDetails;
    private Thought testThought;

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
        testThought = Thought.builder()
                .id(1L)
                .content("Test Content")
                .mood("Happy")
                .user(testUser)
                .status(PublicationStatus.PUBLISHED)
                .build();
    }

    @Test
    void createThought_ReturnsCreatedThought() throws Exception {
        when(thoughtService.create(any(Thought.class))).thenReturn(testThought);

        mockMvc.perform(post("/api/v1/thoughts")
                .with(user(customUserDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testThought)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.content").value("Test Content"));

        verify(thoughtService, times(1)).create(any(Thought.class));
    }

    @Test
    void createThought_WithInvalidData_ReturnsBadRequest() throws Exception {
        com.aftermidnight.dto.ThoughtRequest invalidRequest = com.aftermidnight.dto.ThoughtRequest.builder()
                .content("") // Blank content
                .build();

        mockMvc.perform(post("/api/v1/thoughts")
                .with(user(customUserDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.data.content").exists());

        verify(thoughtService, never()).create(any(Thought.class));
    }

    @Test
    void getAllByUser_ReturnsThoughtPage() throws Exception {
        Page<Thought> thoughtPage = new PageImpl<>(List.of(testThought));
        when(thoughtService.getAllByUser(any(User.class), any(Pageable.class))).thenReturn(thoughtPage);

        mockMvc.perform(get("/api/v1/thoughts")
                .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].id").value(1))
                .andExpect(jsonPath("$.data.content[0].content").value("Test Content"))
                .andExpect(jsonPath("$.data.totalElements").value(1));
    }

    @Test
    void getAllByUserAndStatus_ReturnsThoughtPage() throws Exception {
        Page<Thought> thoughtPage = new PageImpl<>(List.of(testThought));
        when(thoughtService.getAllByUserAndStatus(any(User.class), eq(PublicationStatus.PUBLISHED), any(Pageable.class))).thenReturn(thoughtPage);

        mockMvc.perform(get("/api/v1/thoughts")
                .param("status", "PUBLISHED")
                .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].status").value("PUBLISHED"));
    }

    @Test
    void getById_ReturnsThought_WhenFoundAndPublished() throws Exception {
        when(thoughtService.getById(1L)).thenReturn(Optional.of(testThought));

        mockMvc.perform(get("/api/v1/thoughts/1")
                .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void getByIdV1_ReturnsThought_WhenFoundAndPublished() throws Exception {
        when(thoughtService.getById(1L)).thenReturn(Optional.of(testThought));

        mockMvc.perform(get("/api/v1/thoughts/1")
                .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void getById_ReturnsForbidden_WhenDraftAndNotOwner() throws Exception {
        testThought.setStatus(PublicationStatus.DRAFT);
        testThought.setUser(otherUser);
        when(thoughtService.getById(1L)).thenReturn(Optional.of(testThought));

        mockMvc.perform(get("/api/v1/thoughts/1")
                .with(user(customUserDetails)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value("UNAUTHORIZED"));
    }

    @Test
    void updateStatus_ReturnsUpdatedThought() throws Exception {
        when(thoughtService.getById(1L)).thenReturn(Optional.of(testThought));
        testThought.setStatus(PublicationStatus.DRAFT);
        when(thoughtService.updateStatus(eq(1L), eq(PublicationStatus.DRAFT))).thenReturn(testThought);

        mockMvc.perform(patch("/api/v1/thoughts/1/status")
                .param("status", "DRAFT")
                .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("DRAFT"));
    }

    @Test
    void deleteThought_ReturnsOk() throws Exception {
        when(thoughtService.getById(1L)).thenReturn(Optional.of(testThought));
        doNothing().when(thoughtService).delete(1L);

        mockMvc.perform(delete("/api/v1/thoughts/1")
                .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Thought deleted successfully"));

        verify(thoughtService, times(1)).delete(1L);
    }

    @Test
    void incrementViewCount_ReturnsOk() throws Exception {
        when(thoughtService.getById(1L)).thenReturn(Optional.of(testThought));
        doNothing().when(thoughtService).incrementViewCount(1L);

        mockMvc.perform(post("/api/v1/thoughts/1/view")
                .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("View count incremented"));

        verify(thoughtService, times(1)).incrementViewCount(1L);
    }

    @Test
    void endpoints_AreSecured() throws Exception {
        when(thoughtService.getAllPublished(any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));
        
        // GET is now public
        mockMvc.perform(get("/api/v1/thoughts"))
                .andExpect(status().isOk());
                
        // POST is still secured
        mockMvc.perform(post("/api/v1/thoughts"))
                .andExpect(status().isForbidden());
    }
}
