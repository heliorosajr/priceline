package com.priceline.role.enums;

import lombok.Getter;

public enum MessageEnum {
	
	// Custom exception = Entity not found
    EXCEPTION_ENTITY_NOT_FOUND_DESCRIPTION("exception.entityNotFound.description"),
    EXCEPTION_ENTITY_NOT_FOUND_ERR("exception.entityNotFound.err"),
    EXCEPTION_ENTITY_NOT_FOUND_HELP("exception.entityNotFound.help"),
	
	// Role
	ROLE_ERROR_FIND_BY_ID_HELP("role.error.findById.help"),
    ROLE_ERROR_FIND_ALL_HELP("role.error.findAll.help"),
	
	// Unknown
    UNKNOWN_ERROR_DESCRIPTION("unexpected.error.description");
	
	@Getter
    private final String code;

    MessageEnum(String code) {
        this.code = code;
    }

}
