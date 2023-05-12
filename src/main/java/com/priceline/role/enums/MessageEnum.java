package com.priceline.role.enums;

import lombok.Getter;

public enum MessageEnum {
	
	// Custom exception = Entity not found
    EXCEPTION_ENTITY_NOT_FOUND_DESCRIPTION("exception.entityNotFound.description"),
    EXCEPTION_ENTITY_NOT_FOUND_ERR("exception.entityNotFound.err"),
    EXCEPTION_ENTITY_NOT_FOUND_HELP("exception.entityNotFound.help"),
    
    // Membership
    MEMBERSHIP_ERROR_FIND_ALL_HELP("membership.error.findAll.help"),
    MEMBERSHIP_ERROR_FIND_BY_ID_HELP("membership.error.findById.help"),
    MEMBERSHIP_ERROR_FIND_MEMBERSHIPS_OF_ROLE_HELP("membership.error.findMembershipsOfRole.help"),
    MEMBERSHIP_ERROR_FIND_ROLE_OF_MEMBERSHIP_HELP("membership.error.findRoleOfMembership.help"),
	MEMBERSHIP_ERROR_DELETE_HELP("membership.error.delete.help"),
    
	// Role
    ROLE_ERROR_FIND_ALL_HELP("role.error.findAll.help"),
	ROLE_ERROR_FIND_BY_ID_HELP("role.error.findById.help"),
	ROLE_ERROR_FIND_DEFAULT_HELP("role.error.findDefault.help"),
    ROLE_ERROR_DELETE_HELP("role.error.delete.help"),
	
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
