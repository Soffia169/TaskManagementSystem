package org.chudinova.sofia.models.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(
        description = "Данные новой задачи",
        example = """
                {
                    "id": 1,
                    "title": "Task_1",
                    "description": "Описание задачи",
                    "author": {
                        "id": 1,
                        "name": "User_1"
                    },
                    "assignee": {
                        "id": 1,
                        "name": "User_1"
                    },
                    "status": "Принято в работу",
                    "priority": "Высокий",
                    "comments": [
                        {
                          "content": "Исправить баг",
                          "author": {
                            "id": 1,
                            "name": "User_1"
                          },
                          "createdAt": "2025-04-14T04:40:57.485Z"
                        }
                    ],
                    "createdAt": "2025-04-14T04:40:57.485Z"
                }
                """
)
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