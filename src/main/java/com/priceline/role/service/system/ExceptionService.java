package com.priceline.role.service.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.priceline.role.enums.MessageEnum;
import com.priceline.role.model.exception.BaseException;
import com.priceline.role.model.exception.PricelineApiException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExceptionService {

    @Autowired
    private MessageService messageService;
    
    public PricelineApiException throwIllegalArgumentException(MessageEnum messageEnum, Object... args) throws PricelineApiException {
    	return throwRuntimeException(new IllegalArgumentException(), messageEnum, args);
    }
    
    public PricelineApiException throwRuntimeException(Exception exception, MessageEnum messageEnum, Object... args) throws PricelineApiException {
    	PricelineApiException ex;

        if(exception instanceof BaseException) {
            ex = getPricelineApiException((BaseException) exception);
        } else if(exception instanceof IllegalArgumentException) {
        	ex = getPricelineApiException(messageEnum, args);
        } else if(exception instanceof PricelineApiException) {
            ex = (PricelineApiException) exception;
        } else {
            ex = getPricelineApiException(exception, messageEnum, args);
        }

        log.error("Priceline API Exception");
        log.error(ex.getMessage());
        log.error(ex.getHelp());
        log.error(ex.getStatus().getReasonPhrase());

        throw ex;
    }

    private PricelineApiException getPricelineApiException(BaseException exception) {
        String description = messageService.getMessage(exception.getDescription(), exception.getDescriptionArgs());
        String errorMessage = messageService.getMessage(exception.getErrorMessage(), exception.getErrorMessageArgs());
        String help = messageService.getMessage(exception.getHelp(), exception.getHelpArgs());

        return new PricelineApiException(description, errorMessage, help, exception.getHttpStatus());
    }

    private PricelineApiException getPricelineApiException(Exception exception, MessageEnum messageEnum, Object[] args) {
        String description = messageService.getMessage(MessageEnum.UNKNOWN_ERROR_DESCRIPTION);
        String help = messageService.getMessage(messageEnum, args);

        return new PricelineApiException(description, exception.getMessage(), help, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    private PricelineApiException getPricelineApiException(MessageEnum messageEnum, Object[] args) {
    	String description = messageService.getMessage(MessageEnum.VALIDATION_FAILURE_DESCRIPTION);
        String help = messageService.getMessage(messageEnum, args);

        return new PricelineApiException(description, help, null, HttpStatus.BAD_REQUEST);
    }

}
