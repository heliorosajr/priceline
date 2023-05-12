package com.priceline.role.service.base;

import com.priceline.role.model.exception.PricelineApiException;

public interface AbstractValidate<U> {

    void validate(U dto) throws PricelineApiException;

}
