package com.aftermidnight.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class PoemTest {

    @Test
    void testPoemBuilderAndGetters() {
        User user = User.builder().id(1L).username("testuser").build();
        LocalDateTime now = LocalDateTime.now();
        
        Poem poem = Poem.builder()
                .id(1L)
                .title("Midnight Whispers")
                .content("Silent nights and starry skies...")
                .createdAt(now)
                .user(user)
                .deleted(false)
                .build();

        assertEquals(1L, poem.getId());
        assertEquals("Midnight Whispers", poem.getTitle());
        assertEquals("Silent nights and starry skies...", poem.getContent());
        assertEquals(now, poem.getCreatedAt());
        assertEquals(user, poem.getUser());
        assertFalse(poem.isDeleted());
        assertEquals(0, poem.getViewCount());
        assertEquals(0, poem.getLikeCount());
    }

    @Test
    void testSetters() {
        Poem poem = new Poem();
        poem.setDeleted(true);
        poem.setViewCount(10);
        poem.setLikeCount(5);
        assertTrue(poem.isDeleted());
        assertEquals(10, poem.getViewCount());
        assertEquals(5, poem.getLikeCount());
    }

    @Test
    void testNoArgsConstructor() {
        Poem poem = new Poem();
        assertNotNull(poem);
    }
}
