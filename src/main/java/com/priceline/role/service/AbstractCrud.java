package com.priceline.role.service;

import java.util.List;

import com.priceline.role.model.exception.PricelineApiException;

public interface AbstractCrud<T, U> {

	// ----------------------------------------------------
    // Read
    // ----------------------------------------------------
    T findByUid(String uid) throws PricelineApiException;

    List<T> findAll() throws PricelineApiException;

    // ----------------------------------------------------
    // Persist
    // ----------------------------------------------------
    T save(U dto) throws PricelineApiException;

    T update(U dto, String uid) throws PricelineApiException;

    // ----------------------------------------------------
    // Delete
    // ----------------------------------------------------
    void delete(String id) throws PricelineApiException;

    // ----------------------------------------------------
    // Validation
    // ----------------------------------------------------
    void validate(U dto) throws PricelineApiException;

}
