package com.movie.movie_streaming.handler;



import com.movie.movie_streaming.exceptions.*;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

import static com.movie.movie_streaming.handler.BussinesErrorCode.*;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handleException(LockedException exp){
        return ResponseEntity.status(UNAUTHORIZED).body(
                ExceptionResponse.builder()
                        .businessErrorCode(ACCOUNT_LOCKED.getCode())
                        .businessErrorDescription(ACCOUNT_LOCKED.getDescription())
                        .error(exp.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handleException(DisabledException exp){
        return ResponseEntity.status(UNAUTHORIZED).body(
                ExceptionResponse.builder()
                        .businessErrorCode(ACCOUNT_DISABLED.getCode())
                        .businessErrorDescription(ACCOUNT_DISABLED.getDescription())
                        .error(exp.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleException(BadCredentialsException exp){
        return ResponseEntity.status(UNAUTHORIZED).body(
                ExceptionResponse.builder()
                        .businessErrorCode(BAD_CREDENTIALS.getCode())
                        .businessErrorDescription(BAD_CREDENTIALS.getDescription())
                        .error(BAD_CREDENTIALS.getDescription())
                        .build()
        );
    }
    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<ExceptionResponse> handleException(IncorrectPasswordException exp){
        return ResponseEntity.status(BAD_REQUEST).body(
                ExceptionResponse.builder()
                        .businessErrorCode(INCORRECT_CURRENT_PASSWORD.getCode())
                        .businessErrorDescription(INCORRECT_CURRENT_PASSWORD.getDescription())
                        .error(INCORRECT_CURRENT_PASSWORD.getDescription())
                        .build()
        );
    }

    @ExceptionHandler(NewPasswordDoesNotMatchException.class)
    public ResponseEntity<ExceptionResponse> handleException(NewPasswordDoesNotMatchException exp){
        return ResponseEntity.status(BAD_REQUEST).body(
                ExceptionResponse.builder()
                        .businessErrorCode(NEW_PASSWORD_DOES_NOT_MATCH.getCode())
                        .businessErrorDescription(NEW_PASSWORD_DOES_NOT_MATCH.getDescription())
                        .error(NEW_PASSWORD_DOES_NOT_MATCH.getDescription())
                        .build()
        );
    }
    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse> handleException(MessagingException exp){
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                ExceptionResponse.builder()
                        .error(exp.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException exp){
        Set<String> errors = new HashSet<>();
        exp.getBindingResult()
                .getAllErrors()
                .forEach(error -> errors.add(error.getDefaultMessage()));

        return ResponseEntity.status(BAD_REQUEST).body(
                ExceptionResponse.builder()
                        .validationErrors(errors)
                        .build()
        );
    }
    @ExceptionHandler({
            CommentNotFoundException.class,
            MovieNotFoundException.class,
            DirectorNotFoundException.class,
            ActorNotFoundException.class,
            UsernameNotFoundException.class
    })
    public ResponseEntity<ExceptionResponse> handleException(EntityNotFoundException exp){
        return ResponseEntity.status(NOT_FOUND).body(
                ExceptionResponse.builder()
                        .error(exp.getMessage())
                        .build()
        );
    }

    @ExceptionHandler({
            MovieAlreadyExistsException.class,
            UsernameAlreadyTakenException.class,
            EmailAlreadyTakenException.class
    })
    public ResponseEntity<ExceptionResponse> handleException(RuntimeException exp){
        return ResponseEntity.status(CONFLICT).body(
                ExceptionResponse.builder()
                        .error(exp.getMessage())
                        .build()
        );
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exp){
        exp.printStackTrace();
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                ExceptionResponse.builder()
                        .businessErrorDescription("Internal server error")
                        .error(exp.getMessage())
                        .build()
        );
    }

}