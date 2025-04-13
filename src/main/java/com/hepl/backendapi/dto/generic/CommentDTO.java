package com.hepl.backendapi.dto.generic;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private Long id;

    private Long customerId;

    private Long productId;

    private int rating;

    private String comment;

    private LocalDateTime createdAt;
}
