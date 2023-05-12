package com.priceline.role.service.base;

import com.priceline.role.model.exception.PricelineApiException;

public interface AbstractSave<T, U> {

    T save(U dto) throws PricelineApiException;

}
