package org.chudinova.sofia.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.chudinova.sofia.entities.User;
import org.chudinova.sofia.exceptions.*;
import org.chudinova.sofia.models.requests.TaskRequest;
import org.chudinova.sofia.models.responses.InvalidRequestExceptionResponse;
import org.chudinova.sofia.models.responses.NotFoundExceptionResponse;
import org.chudinova.sofia.models.responses.SecurityExceptionResponse;
import org.chudinova.sofia.models.responses.TaskResponse;
import org.chudinova.sofia.models.requests.UpdateTaskRequest;
import org.chudinova.sofia.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления задачами
 */
@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Задачи", description = "Управление задачами")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    /**
     * Создание новой задачи
     * @param request DTO с данными новой задачи
     * @param currentUser Текущий аутентифицированный пользователь
     * @return DTO созданной задачи
     * @throws UserNotFoundException если пользователь (исполнитель/автор) не найден
     * @throws StatusNotFoundException если статус не найден
     * @throws PriorityNotFoundException если приоритет не найден
     * @throws InvalidRequestException если введены некорретные данные
     * @throws AccessDeniedException если нет прав на создание задачи
     */
    @Operation(
            summary = "Создать задачу",
            description = "Доступно только администраторам"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Успешное создание",
                    content = @Content(schema = @Schema(implementation = TaskResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные",
                    content = @Content(schema = @Schema(implementation = InvalidRequestExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Отказано в доступе",
                    content = @Content(schema = @Schema(implementation = SecurityExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(schema = @Schema(implementation = NotFoundExceptionResponse.class))
            )
    })
    @SecurityRequirement(name = "JWT")
    @PostMapping("/newTask")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request,
                                                   @AuthenticationPrincipal User currentUser) {
        return new ResponseEntity<>(taskService.createTask(request, currentUser), HttpStatus.CREATED);
    }

    /**
     * Редактирование задачи
     * @param request DTO с данными для задачи
     * @param currentUser Текущий аутентифицированный пользователь
     * @return DTO обновлённой задачи
     * @throws UserNotFoundException если пользователь (исполнитель/автор) не найден
     * @throws StatusNotFoundException если статус не найден
     * @throws PriorityNotFoundException если приоритет не найден
     * @throws InvalidRequestException если введены некорретные данные
     * @throws AccessDeniedException если нет прав на создание задачи
     */
    @Operation(
            summary = "Редактировать задачу",
            description = "Изменение данных задачи"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное обновление",
                    content = @Content(schema = @Schema(implementation = TaskResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные",
                    content = @Content(schema = @Schema(implementation = InvalidRequestExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Отказано в доступе",
                    content = @Content(schema = @Schema(implementation = SecurityExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(schema = @Schema(implementation = NotFoundExceptionResponse.class))
            )
    })
    @SecurityRequirement(name = "JWT")
    @PutMapping("/updateTask")
    public ResponseEntity<TaskResponse> updateTask(@Valid @RequestBody UpdateTaskRequest request,
                                                   @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(taskService.updateTask(request, currentUser));
    }

    /**
     * Получение списка задач конкретного автора
     * @param authorId ID автора
     * @return список DTO-задач
     * @throws UserNotFoundException если автор не найден
     * @throws AccessDeniedException если нет прав доступа
     */
    @Operation(
            summary = "Получить задачи конкретного автора",
            description = "Доступно только администратору"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение",
                    content = @Content(schema = @Schema(implementation = TaskResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Отказано в доступе",
                    content = @Content(schema = @Schema(implementation = SecurityExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(schema = @Schema(implementation = NotFoundExceptionResponse.class))
            )
    })
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

    /**
     * Получение списка задач конкретного исполнителя
     * @param assigneeId ID автора
     * @return список DTO-задач
     * @throws UserNotFoundException если исполнитель не найден
     * @throws AccessDeniedException если нет прав доступа
     */
    @Operation(
            summary = "Получить задачи конкретного исполнителя",
            description = "Доступно только администратору"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение",
                    content = @Content(schema = @Schema(implementation = TaskResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Отказано в доступе",
                    content = @Content(schema = @Schema(implementation = SecurityExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(schema = @Schema(implementation = NotFoundExceptionResponse.class))
            )
    })
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

    /**
     * Удаление задачи
     * @param taskId ID задачи
     * @return пустой ответ
     * @throws TaskNotFoundException если задача не найдена
     * @throws AccessDeniedException если нет прав доступа
     */
    @Operation(
            summary = "Удалить задачу",
            description = "Доступно только администратору"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Успешное удаление"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Отказано в доступе",
                    content = @Content(schema = @Schema(implementation = SecurityExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(schema = @Schema(implementation = NotFoundExceptionResponse.class))
            )
    })
    @Parameter(
            name = "taskId",
            description = "ID задачи"
    )
    @SecurityRequirement(name = "JWT")
    @DeleteMapping("/deleteTask/{taskId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTask(@PathVariable long taskId) {
        taskService.deleteTask(taskId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
