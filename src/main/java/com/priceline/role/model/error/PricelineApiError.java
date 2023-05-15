package com.priceline.role.model.error;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

import java.time.LocalDateTime;

import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.priceline.role.model.exception.PricelineApiException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
public class PricelineApiError {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String description;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String errorMessage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String help;

    private final int status;

    private final String reason;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String path;

    @Setter(AccessLevel.NONE)
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private final LocalDateTime timestamp;

    public PricelineApiError(PricelineApiException pricelineApiException, WebRequest webRequest) {
        super();
        this.description = pricelineApiException.getDescription();
        this.errorMessage = pricelineApiException.getMessage();
        this.help = pricelineApiException.getHelp();
        this.status = pricelineApiException.getStatus().value();
        this.reason = pricelineApiException.getStatus().getReasonPhrase();
        this.path = webRequest != null ? ((ServletWebRequest) webRequest).getRequest().getRequestURI() : null;
        this.timestamp = LocalDateTime.now();
    }

}