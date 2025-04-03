package org.chudinova.sofia.models.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {
    private long taskId;
    private String title;
    private String description;
    private long assigneeId;
    private int statusId;
    private int priorityId;
}
