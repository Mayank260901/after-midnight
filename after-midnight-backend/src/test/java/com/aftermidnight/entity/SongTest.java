package com.aftermidnight.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class SongTest {

    @Test
    void testSongEntity() {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .build();

        LocalDateTime now = LocalDateTime.now();
        Song song = Song.builder()
                .id(1L)
                .title("Test Song")
                .lyrics("These are test lyrics")
                .audioUrl("http://example.com/audio.mp3")
                .createdAt(now)
                .user(user)
                .deleted(false)
                .build();

        assertEquals(1L, song.getId());
        assertEquals("Test Song", song.getTitle());
        assertEquals("These are test lyrics", song.getLyrics());
        assertEquals("http://example.com/audio.mp3", song.getAudioUrl());
        assertEquals(now, song.getCreatedAt());
        assertEquals(user, song.getUser());
        assertFalse(song.isDeleted());
        assertEquals(0, song.getViewCount());
        assertEquals(0, song.getLikeCount());

        song.setDeleted(true);
        song.setViewCount(10);
        song.setLikeCount(5);
        assertTrue(song.isDeleted());
        assertEquals(10, song.getViewCount());
        assertEquals(5, song.getLikeCount());
    }

    @Test
    void testNoArgsConstructor() {
        Song song = new Song();
        assertNotNull(song);
    }
}
