package org.chudinova.sofia.controllers;

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
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/newTask")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request,
                                                   @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(taskService.createTask(request, currentUser));
    }

    @PutMapping("/updateTask")
    public ResponseEntity<TaskResponse> updateTask(@RequestBody UpdateTaskRequest request,
                                                   @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(taskService.updateTask(request, currentUser));
    }

    @GetMapping("/getTasksByAuthor/{authorId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TaskResponse>> getTasksByAuthor(@PathVariable long authorId) {
        return ResponseEntity.ok(taskService.getTasksByAuthor(authorId));
    }

    @GetMapping("/getTasksByAssignee/{assigneeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TaskResponse>> getTasksByAssignee(@PathVariable long assigneeId) {
        return ResponseEntity.ok(taskService.getTasksByAssignee(assigneeId));
    }

    @DeleteMapping("/deleteTask")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTask(@RequestBody TaskRequest request) {
        taskService.deleteTask(request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
