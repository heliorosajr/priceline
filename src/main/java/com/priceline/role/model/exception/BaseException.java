package com.priceline.role.model.exception;

import org.springframework.http.HttpStatus;

import com.priceline.role.enums.MessageEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private MessageEnum description;

    private final Object[] descriptionArgs;

    private final MessageEnum errorMessage;

    private final Object[] errorMessageArgs;

    private final MessageEnum help;

    private final Object[] helpArgs;

    private final HttpStatus httpStatus;

}
