package com.aftermidnight.service.impl;

import com.aftermidnight.entity.PublicationStatus;
import com.aftermidnight.entity.Thought;
import com.aftermidnight.entity.User;
import com.aftermidnight.repository.ThoughtRepository;
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
class ThoughtServiceImplTest {

    @Mock
    private ThoughtRepository thoughtRepository;

    @InjectMocks
    private ThoughtServiceImpl thoughtService;

    private User user;
    private Thought thought;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
        thought = Thought.builder().id(1L).content("Test Thought").mood("Happy").user(user).build();
    }

    @Test
    void createThought() {
        when(thoughtRepository.save(any(Thought.class))).thenReturn(thought);
        Thought savedThought = thoughtService.create(thought);
        assertNotNull(savedThought);
        assertEquals("Test Thought", savedThought.getContent());
        assertEquals(PublicationStatus.DRAFT, savedThought.getStatus());
        verify(thoughtRepository, times(1)).save(thought);
    }

    @Test
    void createThought_AsPublished_SetsPublishedAt() {
        thought.setStatus(PublicationStatus.PUBLISHED);
        when(thoughtRepository.save(any(Thought.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Thought savedThought = thoughtService.create(thought);
        assertNotNull(savedThought.getPublishedAt());
        verify(thoughtRepository, times(1)).save(thought);
    }

    @Test
    void getAllByUser() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Thought> page = new PageImpl<>(Arrays.asList(thought));
        when(thoughtRepository.findByUserAndDeletedFalse(user, pageable)).thenReturn(page);
        
        Page<Thought> thoughts = thoughtService.getAllByUser(user, pageable);
        
        assertFalse(thoughts.isEmpty());
        assertEquals(1, thoughts.getTotalElements());
        verify(thoughtRepository, times(1)).findByUserAndDeletedFalse(user, pageable);
    }

    @Test
    void getAllByUserAndStatus() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Thought> page = new PageImpl<>(Arrays.asList(thought));
        when(thoughtRepository.findByUserAndStatusAndDeletedFalse(user, PublicationStatus.PUBLISHED, pageable)).thenReturn(page);

        Page<Thought> thoughts = thoughtService.getAllByUserAndStatus(user, PublicationStatus.PUBLISHED, pageable);

        assertFalse(thoughts.isEmpty());
        verify(thoughtRepository, times(1)).findByUserAndStatusAndDeletedFalse(user, PublicationStatus.PUBLISHED, pageable);
    }

    @Test
    void getById() {
        when(thoughtRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(thought));
        Optional<Thought> foundThought = thoughtService.getById(1L);
        assertTrue(foundThought.isPresent());
        assertEquals(1L, foundThought.get().getId());
    }

    @Test
    void delete() {
        when(thoughtRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(thought));
        when(thoughtRepository.save(any(Thought.class))).thenReturn(thought);
        
        thoughtService.delete(1L);
        
        assertTrue(thought.isDeleted());
        verify(thoughtRepository, times(1)).findByIdAndDeletedFalse(1L);
        verify(thoughtRepository, times(1)).save(thought);
    }

    @Test
    void updateStatus_ToPublished_SetsPublishedAt() {
        when(thoughtRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(thought));
        when(thoughtRepository.save(any(Thought.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Thought updatedThought = thoughtService.updateStatus(1L, PublicationStatus.PUBLISHED);

        assertEquals(PublicationStatus.PUBLISHED, updatedThought.getStatus());
        assertNotNull(updatedThought.getPublishedAt());
        verify(thoughtRepository, times(1)).save(thought);
    }

    @Test
    void incrementViewCount() {
        when(thoughtRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(thought));
        when(thoughtRepository.save(any(Thought.class))).thenReturn(thought);

        thoughtService.incrementViewCount(1L);

        assertEquals(1, thought.getViewCount());
        verify(thoughtRepository, times(1)).findByIdAndDeletedFalse(1L);
        verify(thoughtRepository, times(1)).save(thought);
    }
}
