package com.priceline.role.service.base;

import com.priceline.role.model.exception.PricelineApiException;

public interface AbstractDelete {

    void delete(String uid) throws PricelineApiException;

}
