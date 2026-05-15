package com.example.vehicle.api.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.net.URI;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
    ProblemDetail problem = problem(HttpStatus.BAD_REQUEST, "validation_error", "Dados inválidos");
    problem.setProperty("fields", ex.getBindingResult().getFieldErrors().stream()
        .map(error -> Map.of(
            "field", error.getField(),
            "message", error.getDefaultMessage() == null ? "inválido" : error.getDefaultMessage()))
        .toList());
    return problem;
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ProblemDetail handleConstraintViolation(ConstraintViolationException ex) {
    ProblemDetail problem = problem(HttpStatus.BAD_REQUEST, "validation_error", "Parâmetros inválidos");
    problem.setProperty("fields", ex.getConstraintViolations().stream()
        .map(violation -> Map.of(
            "field", violation.getPropertyPath().toString(),
            "message", violation.getMessage()))
        .toList());
    return problem;
  }

  @ExceptionHandler({
      IllegalArgumentException.class,
      MethodArgumentTypeMismatchException.class,
      MissingServletRequestParameterException.class
  })
  public ProblemDetail handleBadRequest(Exception ex) {
    return problem(HttpStatus.BAD_REQUEST, "bad_request", ex.getMessage());
  }

  @ExceptionHandler(ResourceConflictException.class)
  public ProblemDetail handleConflict(ResourceConflictException ex) {
    return problem(HttpStatus.CONFLICT, "conflict", ex.getMessage());
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
    return problem(HttpStatus.NOT_FOUND, "not_found", ex.getMessage());
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ProblemDetail handleDataIntegrity(DataIntegrityViolationException ex) {
    return problem(HttpStatus.CONFLICT, "conflict", "Registro duplicado ou vinculado a outros dados");
  }

  @ExceptionHandler(Exception.class)
  public ProblemDetail handleUnexpected(Exception ex) {
    return problem(HttpStatus.INTERNAL_SERVER_ERROR, "internal_server_error", "Erro interno do servidor");
  }

  private ProblemDetail problem(HttpStatus status, String type, String detail) {
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
    problem.setType(URI.create("urn:problem:" + type));
    problem.setTitle(type);
    return problem;
  }
}
