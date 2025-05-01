package com.hepl.backendapi.service;

import com.hepl.backendapi.dto.generic.CommentDTO;
import com.hepl.backendapi.dto.generic.UserDTO;
import com.hepl.backendapi.dto.post.CommentCreateDTO;
import com.hepl.backendapi.entity.dbservices.CommentEntity;
import com.hepl.backendapi.exception.RessourceNotFoundException;
import com.hepl.backendapi.mappers.CommentMapper;
import com.hepl.backendapi.repository.dbservices.CommentRepository;
import com.hepl.backendapi.repository.dbtransac.ProductRepository;
import com.hepl.backendapi.utils.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
        public void testCreateComment_Success() {
            // Arrange
            CommentCreateDTO commentCreateDTO = CommentCreateDTO.builder()
                    .productId(1L)
                    .rating(5)
                    .comment("Great product!")
                    .build();

            Long userId = 1L;
            // Créer un mock de UserDTO
            UserDTO userDTO = mock(UserDTO.class);
            when(userDTO.getId()).thenReturn(userId); // Suppose que UserDTO a une méthode getId()

            CommentEntity commentEntity = CommentEntity.builder()
                    .customerId(userId)
                    .productId(commentCreateDTO.getProductId())
                    .rating(commentCreateDTO.getRating())
                    .comment(commentCreateDTO.getComment())
                    .createdAt(LocalDateTime.now())
                    .build();

            CommentDTO commentDTO = CommentDTO.builder()
                    .id(1L)
                    .customerId(userId)
                    .productId(commentCreateDTO.getProductId())
                    .rating(commentCreateDTO.getRating())
                    .comment(commentCreateDTO.getComment())
                    .createdAt(commentEntity.getCreatedAt())
                    .build();

            // Mocker la méthode statique SecurityUtils.getCurrentUserDetails()
            try (MockedStatic<SecurityUtils> mockedStatic = Mockito.mockStatic(SecurityUtils.class)) {
                mockedStatic.when(SecurityUtils::getCurrentUserDetails).thenReturn(userDTO);

                when(productRepository.existsById(commentCreateDTO.getProductId())).thenReturn(true);
                when(commentRepository.save(any(CommentEntity.class))).thenReturn(commentEntity);
                when(commentMapper.toDTO(commentEntity)).thenReturn(commentDTO);

                // Act
                CommentDTO result = commentService.createComment(commentCreateDTO);

                // Assert
                assertNotNull(result);
                assertEquals(commentDTO, result);
                verify(productRepository).existsById(commentCreateDTO.getProductId());
                verify(commentRepository).save(any(CommentEntity.class));
                verify(commentMapper).toDTO(commentEntity);
            }
        }

        @Test
        public void testCreateComment_ProductNotFound() {
            CommentCreateDTO commentCreateDTO = CommentCreateDTO.builder()
                    .productId(1L)
                    .rating(5)
                    .comment("Great product!")
                    .build();
        
            when(productRepository.existsById(commentCreateDTO.getProductId())).thenReturn(false);
        
            RessourceNotFoundException exception = assertThrows(RessourceNotFoundException.class, () -> {
                commentService.createComment(commentCreateDTO);
            });
        
            assertEquals("Ressource(s) (Product ID not found: " + commentCreateDTO.getProductId() + ")", exception.getMessage());
        
            verify(productRepository).existsById(commentCreateDTO.getProductId());
            verify(commentRepository, never()).save(any());
            verify(commentMapper, never()).toDTO(any());
        }
        

    @Test
    public void testGetCommentsByProductId_Success() {
        Long productId = 1L;
        List<CommentEntity> commentEntities = Arrays.asList(
            CommentEntity.builder().id(1L).productId(productId).rating(5).comment("Great product!").createdAt(LocalDateTime.now()).customerId(1L).build(),
            CommentEntity.builder().id(2L).productId(productId).rating(4).comment("Good value.").createdAt(LocalDateTime.now()).customerId(1L).build()
        );

        List<CommentDTO> commentDTOs = Arrays.asList(
            CommentDTO.builder().id(1L).productId(productId).rating(5).comment("Great product!").createdAt(LocalDateTime.now()).build(),
            CommentDTO.builder().id(2L).productId(productId).rating(4).comment("Good value.").createdAt(LocalDateTime.now()).build()
        );

        when(commentRepository.findByProductId(productId)).thenReturn(commentEntities);
        when(commentMapper.toDTO(any(CommentEntity.class))).thenAnswer(invocation -> {
            CommentEntity entity = invocation.getArgument(0);
            return CommentDTO.builder()
                    .id(entity.getId())
                    .productId(entity.getProductId())
                    .rating(entity.getRating())
                    .comment(entity.getComment())
                    .createdAt(entity.getCreatedAt())
                    .build();
        });

        List<CommentDTO> result = commentService.getCommentsByProductId(productId);

        assertNotNull(result);
        assertEquals(commentDTOs.size(), result.size());
        verify(commentRepository).findByProductId(productId);
    }

    @Test
    public void testGetCommentsByProductId_NoComments() {
        Long productId = 1L;

        when(commentRepository.findByProductId(productId)).thenReturn(Collections.emptyList());

        List<CommentDTO> result = commentService.getCommentsByProductId(productId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(commentRepository).findByProductId(productId);
    }
}