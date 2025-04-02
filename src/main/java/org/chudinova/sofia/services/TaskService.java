package org.chudinova.sofia.services;

import lombok.RequiredArgsConstructor;
import org.chudinova.sofia.entities.*;
import org.chudinova.sofia.exceptions.*;
import org.chudinova.sofia.models.requests.TaskRequest;
import org.chudinova.sofia.models.requests.UpdateTaskRequest;
import org.chudinova.sofia.models.responses.CommentInfo;
import org.chudinova.sofia.models.responses.TaskResponse;
import org.chudinova.sofia.models.responses.UserInfo;
import org.chudinova.sofia.repositories.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final StatusRepository statusRepository;
    private final PriorityRepository priorityRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public TaskResponse createTask(TaskRequest request, User currentUser) {
        validateRequest(request);

        User assignee = userRepository.findById(request.getAssigneeId())
                .orElseThrow(() -> new UserNotFoundException("User with id " + request.getAssigneeId() + " not found"));
        Status status = statusRepository.findById(request.getStatusId())
                .orElseThrow(() -> new StatusNotFoundException("Status with id " + request.getStatusId() + " not found"));
        Priority priority = priorityRepository.findById(request.getPriorityId())
                .orElseThrow(() -> new PriorityNotFoundException("Priority with id " + request.getPriorityId() + " not found"));

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .author(currentUser)
                .assignee(assignee)
                .status(status)
                .priority(priority)
                .build();
        return convertToDto(taskRepository.save(task), "CREATE");
    }

    @Transactional
    public List<TaskResponse> getTasksByAuthor(long authorId) {
        userRepository.findById(authorId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + authorId + " not found"));
        List<Task> tasks = taskRepository.findByAuthorId(authorId);
        if (tasks.isEmpty()) {
            return List.of();
        }
        ArrayList<TaskResponse> tasksResponse = new ArrayList<>();
        for (Task task : tasks) {
            tasksResponse.add(convertToDto(task, "GET"));
        }
        return tasksResponse;
    }

    @Transactional
    public List<TaskResponse> getTasksByAssignee(long assigneeId) {
        userRepository.findById(assigneeId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + assigneeId + " not found"));
        List<Task> tasks = taskRepository.findByAssigneeId(assigneeId);
        if (tasks.isEmpty()) {
            return List.of();
        }
        ArrayList<TaskResponse> tasksResponse = new ArrayList<>();
        for (Task task : tasks) {
            tasksResponse.add(convertToDto(task, "GET"));
        }
        return tasksResponse;
    }

    @Transactional
    public TaskResponse updateTask(UpdateTaskRequest request, User currentUser) {
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + request.getTaskId() + " not found"));

        if (!currentUser.isAdmin() && !Objects.equals(task.getAssignee().getId(), currentUser.getId())) {
            throw new AccessDeniedException("Нет прав на изменение задачи");
        }

        if (currentUser.isAdmin()) {
            updateAdminFields(task, request, currentUser);
        }

        if (Objects.equals(task.getAssignee().getId(), currentUser.getId())) {
            updateAssigneeFields(task, request, currentUser);
        }
        return convertToDto(taskRepository.save(task), "UPDATE");
    }

    @Transactional
    public void deleteTask(TaskRequest request) {
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new TaskNotFoundException("Task with id '" + request.getTaskId() +"' not found"));
        taskRepository.delete(task);
    }

    private TaskResponse convertToDto(Task task, String flag) {
        TaskResponse.TaskResponseBuilder builder = TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .author(mapToUserInfo(task.getAuthor()))
                .assignee(mapToUserInfo(task.getAssignee()))
                .status(task.getStatus().getName())
                .priority(task.getPriority().getName());

        switch (flag) {
            case "CREATE" -> builder.createdAt(task.getCreatedAt());
            case "UPDATE" -> builder.updatedAt(task.getUpdatedAt())
                    .comments(task.getComments().isEmpty() ? null : mapToCommentInfo(task.getComments()));
            case "GET" -> builder.createdAt(task.getCreatedAt())
                    .updatedAt(task.getUpdatedAt())
                    .comments(task.getComments().isEmpty() ? null : mapToCommentInfo(task.getComments()));
        }
        return builder.build();
    }

    private List<CommentInfo> mapToCommentInfo(List<Comment> comments) {
        List<CommentInfo> commentsInfo = new ArrayList<>();
        for (Comment comment : comments) {
            commentsInfo.add(
                    CommentInfo.builder()
                            .content(comment.getContent())
                            .author(mapToUserInfo(comment.getAuthor()))
                            .createdAt(comment.getCreatedAt())
                            .build()
            );
        }
        return commentsInfo;
    }

    private UserInfo mapToUserInfo(User user) {
        return UserInfo.builder()
                .id(user.getId())
                .name(user.getUsername())
                .build();
    }

    private void validateRequest(TaskRequest request) {
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new InvalidRequestException("Заголовок задачи обязателен");
        }

        if (request.getTitle().length() > 255) {
            throw new InvalidRequestException("Заголовок не должен превышать 255 символов");
        }
    }

    private void updateAdminFields(Task task, UpdateTaskRequest request, User user) {
        Optional.ofNullable(request.getTitle()).ifPresent(task::setTitle);
        Optional.ofNullable(request.getDescription()).ifPresent(task::setDescription);

        if (request.getStatusId() != null) {
            updateTaskStatus(request, task);
        }

        if (request.getPriorityId() != null) {
            Priority priority = priorityRepository.findById(request.getPriorityId())
                    .orElseThrow(() -> new PriorityNotFoundException(
                            "Priority with id '" + request.getPriorityId() + "' not found"));
            task.setPriority(priority);
        }

        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new UserNotFoundException(
                            "User with id " + request.getAssigneeId() + " not found"));
            task.setAssignee(assignee);
        }

        if (request.getNewComment() != null && !request.getNewComment().isBlank()) {
            addComment(request, task, user);
        }
    }

    private void updateAssigneeFields(Task task, UpdateTaskRequest request, User user) {
        if (request.getStatusId() != null) {
            updateTaskStatus(request, task);
        }

        if (request.getNewComment() != null && !request.getNewComment().isBlank()) {
            addComment(request, task, user);
        }
    }

    private void updateTaskStatus(UpdateTaskRequest request, Task task) {
        Status status = statusRepository.findById(request.getStatusId()).orElseThrow();
        task.setStatus(status);
    }

    private void addComment(UpdateTaskRequest request, Task task, User user) {
        Comment comment = Comment.builder()
                .content(request.getNewComment())
                .task(task)
                .author(user)
                .build();
        commentRepository.save(comment);
        task.getComments().add(comment);
    }
}
