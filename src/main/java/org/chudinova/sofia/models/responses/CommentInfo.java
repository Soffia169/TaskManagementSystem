package org.chudinova.sofia.models.responses;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentInfo {
    private String content;
    private UserInfo author;
    private LocalDateTime createdAt;
}
