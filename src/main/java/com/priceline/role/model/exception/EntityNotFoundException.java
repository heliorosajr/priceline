package com.priceline.role.model.exception;

import org.springframework.http.HttpStatus;

import com.priceline.role.enums.MessageEnum;

public class EntityNotFoundException extends BaseException {

    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(Object id) {
        super(
                MessageEnum.EXCEPTION_ENTITY_NOT_FOUND_DESCRIPTION, null, // description
                MessageEnum.EXCEPTION_ENTITY_NOT_FOUND_ERR, new Object[] { id }, // error message
                MessageEnum.EXCEPTION_ENTITY_NOT_FOUND_HELP, null, // help
                HttpStatus.NOT_FOUND);
    }

}
