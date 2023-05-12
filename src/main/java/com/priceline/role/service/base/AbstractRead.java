package com.priceline.role.service.base;

import java.util.List;

import com.priceline.role.model.exception.PricelineApiException;

public interface AbstractRead<T> {

    T findByUid(String uid) throws PricelineApiException;

    List<T> findAll() throws PricelineApiException;

}
