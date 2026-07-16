package com.well.tech.task.manager.common.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.well.tech.task.manager.common.exceptions.resource.ResourceNotFoundException;
import com.well.tech.task.manager.common.exceptions.validation.*;
import com.well.tech.task.manager.common.exceptions.auth.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler =
            new GlobalExceptionHandler();

    private static class TestBaseException extends BaseException {

        public TestBaseException(String message, int status) {
            super(message, status);
        }
    }

    @Test
    void shouldHandleBaseException() {

        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI())
                .thenReturn("/api/users");

        BaseException exception =
                new TestBaseException(
                        "User not found",
                        404
                );

        ResponseEntity<ApiError> response =
                handler.handleBaseException(
                        exception,
                        request
                );

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(response.getBody())
                .isNotNull();

        assertThat(response.getBody().message())
                .isEqualTo("User not found");

        verify(request)
                .getRequestURI();
    }

    @Test
    void shouldHandleGenericException() {

        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI())
                .thenReturn("/api/error");

        ResponseEntity<ApiError> response =
                handler.handleGenericException(
                        new RuntimeException(),
                        request
                );

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

        assertThat(response.getBody())
                .isNotNull();

        assertThat(response.getBody().message())
                .isEqualTo(
                        "Unexpected error occurred"
                );
    }

    @Test
    void shouldHandleTypeMismatchException() {

        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI())
                .thenReturn("/api/users/test");

        MethodArgumentTypeMismatchException exception =
                new MethodArgumentTypeMismatchException(
                        "abc",
                        Long.class,
                        "id",
                        mock(MethodParameter.class),
                        new IllegalArgumentException()
                );

        ResponseEntity<ApiError> response =
                handler.handleTypeMismatch(
                        exception,
                        request
                );

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(response.getBody())
                .isNotNull();

        assertThat(response.getBody().message())
                .isEqualTo(
                        "Invalid value 'abc' for parameter 'id'"
                );
    }

    @Test
    void shouldHandleValidationExceptionTypeMismatch() {

        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI())
                .thenReturn("/api/users");

        FieldError fieldError =
                spy(
                        new FieldError(
                                "user",
                                "age",
                                "invalid"
                        )
                );

        when(fieldError.getCode())
                .thenReturn("typeMismatch");

        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(
                        new Object(),
                        "user"
                );

        bindingResult.addError(fieldError);

        MethodArgumentNotValidException exception =
                mock(MethodArgumentNotValidException.class);

        when(exception.getBindingResult())
                .thenReturn(bindingResult);

        ResponseEntity<ApiError> response =
                handler.handleValidationException(
                        exception,
                        request
                );

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(response.getBody())
                .isNotNull();

        assertThat(response.getBody().message())
                .isEqualTo(
                        "Invalid value for parameter 'age'"
                );
    }

    @Test
    void shouldHandleResourceNotFoundException() {

        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI())
                .thenReturn("/api/users/10");


        BaseException exception =
                new ResourceNotFoundException(
                        "User not found"
                );


        ResponseEntity<ApiError> response =
                handler.handleBaseException(
                        exception,
                        request
                );


        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);


        assertThat(response.getBody())
                .isNotNull();


        assertThat(response.getBody().status())
                .isEqualTo(404);


        assertThat(response.getBody().message())
                .isEqualTo("User not found");


        assertThat(response.getBody().path())
                .isEqualTo("/api/users/10");


        verify(request)
                .getRequestURI();
    }

    @Test
    void shouldHandleInvalidParameterException() {

        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI())
                .thenReturn("/api/users");


        BaseException exception =
                new InvalidParameterException(
                        "Invalid email parameter"
                );


        ResponseEntity<ApiError> response =
                handler.handleBaseException(
                        exception,
                        request
                );


        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);


        assertThat(response.getBody())
                .isNotNull();


        assertThat(response.getBody().status())
                .isEqualTo(400);


        assertThat(response.getBody().message())
                .isEqualTo("Invalid email parameter");


        assertThat(response.getBody().path())
                .isEqualTo("/api/users");


        verify(request)
                .getRequestURI();
    }

    @Test
    void shouldHandleValidationException() {

        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI())
                .thenReturn("/api/users");


        BaseException exception =
                new ValidationException(
                        "Email is required"
                );


        ResponseEntity<ApiError> response =
                handler.handleBaseException(
                        exception,
                        request
                );


        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);


        assertThat(response.getBody())
                .isNotNull();


        assertThat(response.getBody().status())
                .isEqualTo(400);


        assertThat(response.getBody().message())
                .isEqualTo("Email is required");


        assertThat(response.getBody().path())
                .isEqualTo("/api/users");


        verify(request)
                .getRequestURI();
    }

    @Test
    void shouldHandleEmailAlreadyExistsException() {

        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI())
                .thenReturn("/api/users");


        BaseException exception =
                new EmailAlreadyExistsException(
                        "Email already exists"
                );


        ResponseEntity<ApiError> response =
                handler.handleBaseException(
                        exception,
                        request
                );


        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.CONFLICT);


        assertThat(response.getBody())
                .isNotNull();


        assertThat(response.getBody().status())
                .isEqualTo(409);


        assertThat(response.getBody().message())
                .isEqualTo("Email already exists");


        assertThat(response.getBody().path())
                .isEqualTo("/api/users");


        verify(request)
                .getRequestURI();
    }

    @Test
    void shouldHandleInvalidCredentialsException() {

        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI())
                .thenReturn("/api/auth/login");

        BaseException exception =
                new InvalidCredentialsException(
                        "Invalid credentials"
                );

        ResponseEntity<ApiError> response =
                handler.handleBaseException(
                        exception,
                        request
                );

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(response.getBody())
                .isNotNull();

        assertThat(response.getBody().status())
                .isEqualTo(401);

        assertThat(response.getBody().message())
                .isEqualTo("Invalid credentials");

        assertThat(response.getBody().path())
                .isEqualTo("/api/auth/login");

        verify(request)
                .getRequestURI();
    }

    @Test
    void shouldHandleUserDisabledException() {

        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI())
                .thenReturn("/api/auth/login");

        BaseException exception =
                new UserDisabledException(
                        "User account is disabled"
                );

        ResponseEntity<ApiError> response =
                handler.handleBaseException(
                        exception,
                        request
                );

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody())
                .isNotNull();

        assertThat(response.getBody().status())
                .isEqualTo(403);

        assertThat(response.getBody().message())
                .isEqualTo("User account is disabled");

        assertThat(response.getBody().path())
                .isEqualTo("/api/auth/login");

        verify(request)
                .getRequestURI();
    }

    @Test
    void shouldHandleUserNotFoundException() {

        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI())
                .thenReturn("/api/users/10");

        BaseException exception =
                new UserNotFoundException(
                        "User not found"
                );

        ResponseEntity<ApiError> response =
                handler.handleBaseException(
                        exception,
                        request
                );

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(response.getBody())
                .isNotNull();

        assertThat(response.getBody().status())
                .isEqualTo(404);

        assertThat(response.getBody().message())
                .isEqualTo("User not found");

        assertThat(response.getBody().path())
                .isEqualTo("/api/users/10");

        verify(request)
                .getRequestURI();
    }
}