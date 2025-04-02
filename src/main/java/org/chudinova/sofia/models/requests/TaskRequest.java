package org.chudinova.sofia.models.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskRequest {
    private long taskId;
    private String title;
    private String description;
    private long assigneeId;
    private int statusId;
    private int priorityId;
}
