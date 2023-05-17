package com.lcwd.electronic.store.exception;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler{

    private Logger logger= LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(ResourcesNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourcesNotFoundException ex){
        logger.info("Exception invoked !!");
        ApiResponseMessage response=
        ApiResponseMessage
                .builder()
                .message(ex.getMessage())
                .success(true)
                .status(HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);


    }


    //method not found exception
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String,Object>> handleMethodArgumentNotValidException (MethodArgumentNotValidException ex){

        List<ObjectError> allErrors= ex.getBindingResult().getAllErrors();
        Map<String,Object> response =new HashMap<>();
        allErrors.stream().forEach(objectError -> {
            String message=objectError.getDefaultMessage();
            String field = ((FieldError) objectError).getField();
            response.put(field,message);
        });


        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }


    //bad api request
    @ExceptionHandler(BadApiRequest.class)
    public ResponseEntity<ApiResponseMessage> badApiRequestExceptionHandler(BadApiRequest ex){
        logger.info("Bad api request !!");
        ApiResponseMessage response=
                ApiResponseMessage
                        .builder()
                        .message(ex.getMessage())
                        .success(false)
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);


    }




}
