package com.aftermidnight.service.impl;

import com.aftermidnight.entity.Poem;
import com.aftermidnight.entity.PublicationStatus;
import com.aftermidnight.entity.User;
import com.aftermidnight.repository.PoemRepository;
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
class PoemServiceImplTest {

    @Mock
    private PoemRepository poemRepository;

    @InjectMocks
    private PoemServiceImpl poemService;

    private User user;
    private Poem poem;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
        poem = Poem.builder().id(1L).title("Test Poem").content("Content").user(user).build();
    }

    @Test
    void createPoem() {
        when(poemRepository.save(any(Poem.class))).thenReturn(poem);
        Poem savedPoem = poemService.create(poem);
        assertNotNull(savedPoem);
        assertEquals("Test Poem", savedPoem.getTitle());
        assertEquals(PublicationStatus.DRAFT, savedPoem.getStatus());
        verify(poemRepository, times(1)).save(poem);
    }

    @Test
    void createPoem_AsPublished_SetsPublishedAt() {
        poem.setStatus(PublicationStatus.PUBLISHED);
        when(poemRepository.save(any(Poem.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Poem savedPoem = poemService.create(poem);
        assertNotNull(savedPoem.getPublishedAt());
        verify(poemRepository, times(1)).save(poem);
    }

    @Test
    void getAllByUser() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Poem> page = new PageImpl<>(Arrays.asList(poem));
        when(poemRepository.findByUserAndDeletedFalse(user, pageable)).thenReturn(page);
        
        Page<Poem> poems = poemService.getAllByUser(user, pageable);
        
        assertFalse(poems.isEmpty());
        assertEquals(1, poems.getTotalElements());
        verify(poemRepository, times(1)).findByUserAndDeletedFalse(user, pageable);
    }

    @Test
    void getAllByUserAndStatus() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Poem> page = new PageImpl<>(Arrays.asList(poem));
        when(poemRepository.findByUserAndStatusAndDeletedFalse(user, PublicationStatus.PUBLISHED, pageable)).thenReturn(page);

        Page<Poem> poems = poemService.getAllByUserAndStatus(user, PublicationStatus.PUBLISHED, pageable);

        assertFalse(poems.isEmpty());
        verify(poemRepository, times(1)).findByUserAndStatusAndDeletedFalse(user, PublicationStatus.PUBLISHED, pageable);
    }

    @Test
    void getById() {
        when(poemRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(poem));
        Optional<Poem> foundPoem = poemService.getById(1L);
        assertTrue(foundPoem.isPresent());
        assertEquals(1L, foundPoem.get().getId());
    }

    @Test
    void delete() {
        when(poemRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(poem));
        when(poemRepository.save(any(Poem.class))).thenReturn(poem);
        
        poemService.delete(1L);
        
        assertTrue(poem.isDeleted());
        verify(poemRepository, times(1)).findByIdAndDeletedFalse(1L);
        verify(poemRepository, times(1)).save(poem);
    }

    @Test
    void updateStatus_ToPublished_SetsPublishedAt() {
        when(poemRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(poem));
        when(poemRepository.save(any(Poem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Poem updatedPoem = poemService.updateStatus(1L, PublicationStatus.PUBLISHED);

        assertEquals(PublicationStatus.PUBLISHED, updatedPoem.getStatus());
        assertNotNull(updatedPoem.getPublishedAt());
        verify(poemRepository, times(1)).save(poem);
    }

    @Test
    void incrementViewCount() {
        when(poemRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(poem));
        when(poemRepository.save(any(Poem.class))).thenReturn(poem);

        poemService.incrementViewCount(1L);

        assertEquals(1, poem.getViewCount());
        verify(poemRepository, times(1)).findByIdAndDeletedFalse(1L);
        verify(poemRepository, times(1)).save(poem);
    }
}
