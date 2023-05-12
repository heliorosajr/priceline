package com.priceline.role.service.base;

import com.priceline.role.model.exception.PricelineApiException;

public interface AbstractUpdate<T, U> {

    T update(U dto, String uid) throws PricelineApiException;

}
