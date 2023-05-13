package com.priceline.role.model.exception;

import org.springframework.http.HttpStatus;

import com.priceline.role.enums.MessageEnum;

public class DefaultRoleNotFoundException extends BaseException {

    private static final long serialVersionUID = 1L;

    public DefaultRoleNotFoundException() {
        super(
                MessageEnum.EXCEPTION_DEFAULT_ROLE_NOT_FOUND_DESCRIPTION, null, // description
                MessageEnum.EXCEPTION_DEFAULT_ROLE_NOT_FOUND_ERR, null, // error message
                MessageEnum.EXCEPTION_DEFAULT_ROLE_NOT_FOUND_HELP, null, // help
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
