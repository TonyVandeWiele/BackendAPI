package com.hepl.backendapi.dto.generic;

import lombok.Data;

@Data
public class PrivateMessageDTO {
    private String fromAccountId;
    private String toAccountId;
    private String message;
}
