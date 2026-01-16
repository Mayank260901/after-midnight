package com.aftermidnight.controller;

import com.aftermidnight.entity.Song;
import com.aftermidnight.entity.PublicationStatus;
import com.aftermidnight.entity.User;
import com.aftermidnight.security.CustomUserDetails;
import com.aftermidnight.service.SongService;
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
class SongControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SongService songService;

    private User testUser;
    private User otherUser;
    private CustomUserDetails customUserDetails;
    private Song testSong;

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
        testSong = Song.builder()
                .id(1L)
                .title("Test Song")
                .lyrics("Test Lyrics")
                .user(testUser)
                .status(PublicationStatus.PUBLISHED)
                .build();
    }

    @Test
    void createSong_ReturnsCreatedSong() throws Exception {
        when(songService.create(any(Song.class))).thenReturn(testSong);

        mockMvc.perform(post("/api/v1/songs")
                .with(user(customUserDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testSong)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Test Song"));

        verify(songService, times(1)).create(any(Song.class));
    }

    @Test
    void createSong_WithInvalidData_ReturnsBadRequest() throws Exception {
        com.aftermidnight.dto.SongRequest invalidRequest = com.aftermidnight.dto.SongRequest.builder()
                .title("") // Blank title
                .lyrics("Valid lyrics")
                .build();

        mockMvc.perform(post("/api/v1/songs")
                .with(user(customUserDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.data.title").exists());

        verify(songService, never()).create(any(Song.class));
    }

    @Test
    void getAllByUser_ReturnsSongPage() throws Exception {
        Page<Song> songPage = new PageImpl<>(List.of(testSong));
        when(songService.getAllByUser(any(User.class), any(Pageable.class))).thenReturn(songPage);

        mockMvc.perform(get("/api/v1/songs")
                .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].id").value(1))
                .andExpect(jsonPath("$.data.content[0].title").value("Test Song"))
                .andExpect(jsonPath("$.data.totalElements").value(1));
    }

    @Test
    void getAllByUserAndStatus_ReturnsSongPage() throws Exception {
        Page<Song> songPage = new PageImpl<>(List.of(testSong));
        when(songService.getAllByUserAndStatus(any(User.class), eq(PublicationStatus.PUBLISHED), any(Pageable.class))).thenReturn(songPage);

        mockMvc.perform(get("/api/v1/songs")
                .param("status", "PUBLISHED")
                .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].status").value("PUBLISHED"));
    }

    @Test
    void getById_ReturnsSong_WhenFoundAndPublished() throws Exception {
        when(songService.getById(1L)).thenReturn(Optional.of(testSong));

        mockMvc.perform(get("/api/v1/songs/1")
                .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Test Song"));
    }

    @Test
    void getByIdV1_ReturnsSong_WhenFoundAndPublished() throws Exception {
        when(songService.getById(1L)).thenReturn(Optional.of(testSong));

        mockMvc.perform(get("/api/v1/songs/1")
                .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Test Song"));
    }

    @Test
    void getById_ReturnsForbidden_WhenDraftAndNotOwner() throws Exception {
        testSong.setStatus(PublicationStatus.DRAFT);
        testSong.setUser(otherUser);
        when(songService.getById(1L)).thenReturn(Optional.of(testSong));

        mockMvc.perform(get("/api/v1/songs/1")
                .with(user(customUserDetails)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value("UNAUTHORIZED"));
    }

    @Test
    void updateStatus_ReturnsUpdatedSong() throws Exception {
        when(songService.getById(1L)).thenReturn(Optional.of(testSong));
        testSong.setStatus(PublicationStatus.DRAFT);
        when(songService.updateStatus(eq(1L), eq(PublicationStatus.DRAFT))).thenReturn(testSong);

        mockMvc.perform(patch("/api/v1/songs/1/status")
                .param("status", "DRAFT")
                .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("DRAFT"));
    }

    @Test
    void deleteSong_ReturnsOk() throws Exception {
        when(songService.getById(1L)).thenReturn(Optional.of(testSong));
        doNothing().when(songService).delete(1L);

        mockMvc.perform(delete("/api/v1/songs/1")
                .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Song deleted successfully"));

        verify(songService, times(1)).delete(1L);
    }

    @Test
    void incrementViewCount_ReturnsOk() throws Exception {
        when(songService.getById(1L)).thenReturn(Optional.of(testSong));
        doNothing().when(songService).incrementViewCount(1L);

        mockMvc.perform(post("/api/v1/songs/1/view")
                .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("View count incremented"));

        verify(songService, times(1)).incrementViewCount(1L);
    }

    @Test
    void endpoints_AreSecured() throws Exception {
        when(songService.getAllPublished(any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));
        
        // GET is now public
        mockMvc.perform(get("/api/v1/songs"))
                .andExpect(status().isOk());
                
        // POST is still secured
        mockMvc.perform(post("/api/v1/songs"))
                .andExpect(status().isForbidden());
    }
}
