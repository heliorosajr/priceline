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
    UNKNOWN_ERROR_DESCRIPTION("unexpected.error.description"),

	// Validation failure
    VALIDATION_FAILURE_DESCRIPTION("validation.failure.description"),
    
    // Validation failure - Required field
 	VALIDATION_FAILURE_REQUIRED_ERR("validation.failure.required.err"),
    
    // Validation failure - String max length
	VALIDATION_FAILURE_STRING_MAX_LENGTH_ERR("validation.failure.stringMaxLenght.err"),
	
	VALIDATION_FAILURE_UNIQUENESS_ERR("validation.failure.uniqueness.err"),;
	
	@Getter
    private final String code;

    MessageEnum(String code) {
        this.code = code;
    }

}
