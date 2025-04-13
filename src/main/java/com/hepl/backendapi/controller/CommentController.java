package com.hepl.backendapi.controller;

import com.hepl.backendapi.dto.generic.CommentDTO;
import com.hepl.backendapi.dto.post.CommentCreateDTO;
import com.hepl.backendapi.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Comment management")
@RequestMapping("/v1")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Create a comment")
    @ApiResponse(responseCode = "200", description = "Comment created successfully")
    @PostMapping("/comment")
    public ResponseEntity<CommentDTO> createComment(@Valid @RequestBody CommentCreateDTO commentCreateDTO) {
        return ResponseEntity.ok(commentService.createComment(commentCreateDTO));
    }

    @Operation(summary = "Get all comments for a specific product")
    @ApiResponse(responseCode = "200", description = "Comments retrieved successfully")
    @GetMapping("/comment/product/{productId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(commentService.getCommentsByProductId(productId));
    }
}
