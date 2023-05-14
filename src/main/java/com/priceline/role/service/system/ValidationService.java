package com.priceline.role.service.system;

import org.springframework.stereotype.Service;

import com.priceline.role.enums.MessageEnum;
import com.priceline.role.model.base.BaseEntity;

@Service
public class ValidationService {
	
    private final ExceptionService exceptionService;
    
    public ValidationService(ExceptionService exceptionService) {
    	this.exceptionService = exceptionService;
    }
	
	public void validateStringMaxLength(String str, String field, int max) {
		if(str != null && str.length() > max) {
	        exceptionService.throwIllegalArgumentException(MessageEnum.VALIDATION_FAILURE_STRING_MAX_LENGTH_ERR, field, max);
		}
    }
	
	public void validateRequired(Object value, String field) {
		boolean throwException = false;
		
        if(value instanceof String) {
            if(((String) value).isEmpty()) {
            	throwException = true;
            }
        } else if(value == null) {
        	throwException = true;
        }
        
        if(throwException) {
	        exceptionService.throwIllegalArgumentException(MessageEnum.VALIDATION_FAILURE_REQUIRED_ERR, field);
        }
    }
	
	public void validateUniqueness(String uid, BaseEntity baseEntity) {
		if(baseEntity == null) {
			return;
		}
		
		if(uid == null || !baseEntity.getUid().equals(uid)) {
			exceptionService.throwIllegalArgumentException(MessageEnum.VALIDATION_FAILURE_UNIQUENESS_ERR);
        }
	}

}
