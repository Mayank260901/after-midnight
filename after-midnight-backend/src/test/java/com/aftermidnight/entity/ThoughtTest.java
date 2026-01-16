package com.aftermidnight.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ThoughtTest {

    @Test
    void testThoughtBuilderAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder().id(1L).username("testuser").build();
        
        Thought thought = Thought.builder()
                .id(1L)
                .content("Test thought content")
                .mood("Calm")
                .createdAt(now)
                .user(user)
                .deleted(false)
                .build();

        assertEquals(1L, thought.getId());
        assertEquals("Test thought content", thought.getContent());
        assertEquals("Calm", thought.getMood());
        assertEquals(now, thought.getCreatedAt());
        assertEquals(user, thought.getUser());
        assertFalse(thought.isDeleted());
        assertEquals(0, thought.getViewCount());
        assertEquals(0, thought.getLikeCount());
    }

    @Test
    void testThoughtSetters() {
        Thought thought = new Thought();
        thought.setId(2L);
        thought.setContent("Updated content");
        thought.setMood("Happy");
        thought.setDeleted(true);
        thought.setViewCount(10);
        thought.setLikeCount(5);
        
        assertEquals(2L, thought.getId());
        assertEquals("Updated content", thought.getContent());
        assertEquals("Happy", thought.getMood());
        assertTrue(thought.isDeleted());
        assertEquals(10, thought.getViewCount());
        assertEquals(5, thought.getLikeCount());
    }
}
