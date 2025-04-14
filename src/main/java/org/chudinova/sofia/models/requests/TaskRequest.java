package org.chudinova.sofia.models.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        description = "Данные новой задачи",
        example = """
                {
                    "title": "Task_1",
                    "description": "Описание задачи",
                    "assigneeId": 1,
                    "status": 1,
                    "priority": 2
                }
                """
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {
    private long taskId;

    @NotNull
    @NotBlank
    private String title;

    @NotNull
    @NotBlank
    private String description;

    @NotNull
    private Long assigneeId;

    @NotNull
    private Integer statusId;

    @NotNull
    private Integer priorityId;
}
