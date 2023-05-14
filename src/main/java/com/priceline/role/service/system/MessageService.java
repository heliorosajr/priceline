package com.priceline.role.service.system;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.priceline.role.enums.MessageEnum;
import com.priceline.role.model.exception.PricelineApiException;

@Service
public class MessageService {

    private final MessageSource messageSource;
    
    public MessageService(MessageSource messageSource) {
    	this.messageSource = messageSource;
    }

    public String getMessage(MessageEnum messageEnum) throws PricelineApiException {
        return getMessage(messageEnum, new Object[] {});
    }

    public String getMessage(MessageEnum messageEnum, Object... args) throws PricelineApiException {
        try {
            return messageSource.getMessage(messageEnum.getCode(), args, LocaleContextHolder.getLocale());
        } catch (Exception exception) {
            throw new PricelineApiException("Configuration error", exception.getMessage(),
                    "Please check message settings", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}