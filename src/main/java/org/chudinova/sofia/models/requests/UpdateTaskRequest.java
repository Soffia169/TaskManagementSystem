package org.chudinova.sofia.models.requests;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskRequest {

    @NotNull
    private Long taskId;
    private String title;
    private String description;
    private Long assigneeId;
    private Integer statusId;
    private Integer priorityId;
    private String newComment;
}
