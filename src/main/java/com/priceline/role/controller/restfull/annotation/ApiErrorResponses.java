package com.priceline.role.controller.restfull.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.http.MediaType;

import com.priceline.role.model.exception.PricelineApiException;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Bad request",
                content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PricelineApiException.class)) }),
        @ApiResponse(responseCode = "404", description = "Resource not found",
                content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PricelineApiException.class)) }),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PricelineApiException.class)) }),
})
public @interface ApiErrorResponses {}
