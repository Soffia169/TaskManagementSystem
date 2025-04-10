package org.chudinova.sofia.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.chudinova.sofia.entities.User;
import org.chudinova.sofia.models.requests.TaskRequest;
import org.chudinova.sofia.models.responses.TaskResponse;
import org.chudinova.sofia.models.requests.UpdateTaskRequest;
import org.chudinova.sofia.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Задачи", description = "Управление задачами")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @Operation(
            summary = "Создать задачу",
            description = "Доступно только администраторам"
    )
    @SecurityRequirement(name = "JWT")
    @PostMapping("/newTask")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request,
                                                   @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(taskService.createTask(request, currentUser));
    }

    @Operation(
            summary = "Редактировать задачу",
            description = "Изменение данных задачи"
    )
    @SecurityRequirement(name = "JWT")
    @PutMapping("/updateTask")
    public ResponseEntity<TaskResponse> updateTask(@RequestBody UpdateTaskRequest request,
                                                   @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(taskService.updateTask(request, currentUser));
    }

    @Operation(
            summary = "Получить задачи конкретного автора",
            description = "Доступно только администратору"
    )
    @Parameter(
            name = "authorId",
            description = "ID автора"
    )
    @SecurityRequirement(name = "JWT")
    @GetMapping("/getTasksByAuthor/{authorId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TaskResponse>> getTasksByAuthor(@PathVariable long authorId) {
        return ResponseEntity.ok(taskService.getTasksByAuthor(authorId));
    }

    @Operation(
            summary = "Получить задачи конкретного исполнителя",
            description = "Доступно только администратору"
    )
    @Parameter(
            name = "assigneeId",
            description = "ID исполнителя"
    )
    @SecurityRequirement(name = "JWT")
    @GetMapping("/getTasksByAssignee/{assigneeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TaskResponse>> getTasksByAssignee(@PathVariable long assigneeId) {
        return ResponseEntity.ok(taskService.getTasksByAssignee(assigneeId));
    }

    @Operation(
            summary = "Удалить задачу",
            description = "Доступно только администратору"
    )
    @SecurityRequirement(name = "JWT")
    @DeleteMapping("/deleteTask")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTask(@Valid @RequestBody TaskRequest request) {
        taskService.deleteTask(request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
