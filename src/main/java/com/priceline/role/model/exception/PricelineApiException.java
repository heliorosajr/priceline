package com.priceline.role.model.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public class PricelineApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	@Getter
    private final String description;

    @Getter
    private final String help;

    @Getter
    private final HttpStatus status;

    /**
     * This is the main <code>Exception</code> class used by this tool.
     * <br>
     * This constructor has all parameters to allow fully customization.
     *
     * @param description a descriptive text to inform what happened. High level, not technical.
     * @param errorMessage the exception message, used for troubleshooting purposes.
     * @param help a debug message with more information to identify the issue.
     * @param status the <code>HttpStatus</code> status that should be returned.
     */
    public PricelineApiException(String description, String errorMessage, String help, HttpStatus status) {
        super(errorMessage);
        this.description = description;
        this.help = help;
        this.status = status;
    }

}