package com.aftermidnight.service.impl;

import com.aftermidnight.entity.PublicationStatus;
import com.aftermidnight.entity.Song;
import com.aftermidnight.entity.User;
import com.aftermidnight.repository.SongRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SongServiceImplTest {

    @Mock
    private SongRepository songRepository;

    @InjectMocks
    private SongServiceImpl songService;

    private User user;
    private Song song;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
        song = Song.builder().id(1L).title("Test Song").lyrics("Lyrics").audioUrl("url").user(user).build();
    }

    @Test
    void createSong() {
        when(songRepository.save(any(Song.class))).thenReturn(song);
        Song savedSong = songService.create(song);
        assertNotNull(savedSong);
        assertEquals("Test Song", savedSong.getTitle());
        assertEquals(PublicationStatus.DRAFT, savedSong.getStatus());
        verify(songRepository, times(1)).save(song);
    }

    @Test
    void createSong_AsPublished_SetsPublishedAt() {
        song.setStatus(PublicationStatus.PUBLISHED);
        when(songRepository.save(any(Song.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Song savedSong = songService.create(song);
        assertNotNull(savedSong.getPublishedAt());
        verify(songRepository, times(1)).save(song);
    }

    @Test
    void getAllByUser() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Song> page = new PageImpl<>(Arrays.asList(song));
        when(songRepository.findByUserAndDeletedFalse(user, pageable)).thenReturn(page);
        
        Page<Song> songs = songService.getAllByUser(user, pageable);
        
        assertFalse(songs.isEmpty());
        assertEquals(1, songs.getTotalElements());
        verify(songRepository, times(1)).findByUserAndDeletedFalse(user, pageable);
    }

    @Test
    void getAllByUserAndStatus() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Song> page = new PageImpl<>(Arrays.asList(song));
        when(songRepository.findByUserAndStatusAndDeletedFalse(user, PublicationStatus.PUBLISHED, pageable)).thenReturn(page);

        Page<Song> songs = songService.getAllByUserAndStatus(user, PublicationStatus.PUBLISHED, pageable);

        assertFalse(songs.isEmpty());
        verify(songRepository, times(1)).findByUserAndStatusAndDeletedFalse(user, PublicationStatus.PUBLISHED, pageable);
    }

    @Test
    void getById() {
        when(songRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(song));
        Optional<Song> foundSong = songService.getById(1L);
        assertTrue(foundSong.isPresent());
        assertEquals(1L, foundSong.get().getId());
    }

    @Test
    void delete() {
        when(songRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(song));
        when(songRepository.save(any(Song.class))).thenReturn(song);
        
        songService.delete(1L);
        
        assertTrue(song.isDeleted());
        verify(songRepository, times(1)).findByIdAndDeletedFalse(1L);
        verify(songRepository, times(1)).save(song);
    }

    @Test
    void updateStatus_ToPublished_SetsPublishedAt() {
        when(songRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(song));
        when(songRepository.save(any(Song.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Song updatedSong = songService.updateStatus(1L, PublicationStatus.PUBLISHED);

        assertEquals(PublicationStatus.PUBLISHED, updatedSong.getStatus());
        assertNotNull(updatedSong.getPublishedAt());
        verify(songRepository, times(1)).save(song);
    }

    @Test
    void incrementViewCount() {
        when(songRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(song));
        when(songRepository.save(any(Song.class))).thenReturn(song);

        songService.incrementViewCount(1L);

        assertEquals(1, song.getViewCount());
        verify(songRepository, times(1)).findByIdAndDeletedFalse(1L);
        verify(songRepository, times(1)).save(song);
    }
}
