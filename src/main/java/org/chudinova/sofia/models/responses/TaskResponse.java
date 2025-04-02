package org.chudinova.sofia.models.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private UserInfo author;
    private UserInfo assignee;
    private String status;
    private String priority;
    private List<CommentInfo> comments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}