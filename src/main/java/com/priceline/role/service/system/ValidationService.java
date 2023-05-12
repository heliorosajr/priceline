package com.priceline.role.service.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.priceline.role.enums.MessageEnum;
import com.priceline.role.model.base.BaseEntity;
import com.priceline.role.model.exception.PricelineApiException;

@Service
public class ValidationService {
	
	@Autowired
    private ExceptionService exceptionService;
	
	public void validateStringMaxLength(String str, String field, int max) {
		if(str != null && str.length() > max) {
	        exceptionService.throwRuntimeException(new IllegalArgumentException(), MessageEnum.VALIDATION_FAILURE_STRING_MAX_LENGTH_ERR, field, max);
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
	        exceptionService.throwRuntimeException(new IllegalArgumentException(), MessageEnum.VALIDATION_FAILURE_REQUIRED_ERR, field);
        }
    }
	
	public void validateUniqueness(String uid, BaseEntity baseEntity) {
		if(baseEntity == null) {
			return;
		}
		
		if(uid == null || !baseEntity.getUid().equals(uid)) {
			exceptionService.throwRuntimeException(new IllegalArgumentException(), MessageEnum.VALIDATION_FAILURE_UNIQUENESS_ERR);
        }
	}

}
