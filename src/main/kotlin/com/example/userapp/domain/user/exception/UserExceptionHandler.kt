package com.example.userapp.domain.user.exception

import jakarta.validation.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class UserExceptionHandler {

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFound(ex: UserNotFoundException): ResponseEntity<ErrorResponse> =
        ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.message ?: "User not found", LocalDateTime.now())
            .let { ResponseEntity.status(HttpStatus.NOT_FOUND).body(it) }

    @ExceptionHandler(UserIdMismatchException::class)
    fun handleUserIdMismatch(ex: UserIdMismatchException): ResponseEntity<ErrorResponse> =
        ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.message ?: "User ID mismatch", LocalDateTime.now())
            .let { ResponseEntity.status(HttpStatus.BAD_REQUEST).body(it) }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errors = ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "Invalid value") }
        return ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation failed", LocalDateTime.now(), errors)
            .let { ResponseEntity.status(HttpStatus.BAD_REQUEST).body(it) }
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(ex: ConstraintViolationException): ResponseEntity<ErrorResponse> {
        val errors = ex.constraintViolations.associate { it.propertyPath.toString() to it.message }
        return ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Constraint violation", LocalDateTime.now(), errors)
            .let { ResponseEntity.status(HttpStatus.BAD_REQUEST).body(it) }
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> =
        ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Invalid JSON format: ${ex.message}", LocalDateTime.now())
            .let { ResponseEntity.status(HttpStatus.BAD_REQUEST).body(it) }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolation(ex: DataIntegrityViolationException): ResponseEntity<ErrorResponse> =
        ErrorResponse(HttpStatus.CONFLICT.value(), "Database constraint violation: ${ex.message}", LocalDateTime.now())
            .let { ResponseEntity.status(HttpStatus.CONFLICT).body(it) }

    @ExceptionHandler(UsernameAlreadyExistsException::class)
    fun handleUsernameAlreadyExistsException(ex: UsernameAlreadyExistsException): ResponseEntity<ErrorResponse> =
        ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.message ?: "Username already exists", LocalDateTime.now())
            .let { ResponseEntity.status(HttpStatus.BAD_REQUEST).body(it) }
}
