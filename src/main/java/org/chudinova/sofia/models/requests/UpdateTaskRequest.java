package org.chudinova.sofia.models.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskRequest {
    private long taskId;
    private String title;
    private String description;
    private Long assigneeId;
    private Integer statusId;
    private Integer priorityId;
    private String newComment;
}
