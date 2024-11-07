package org.example.daos;

import org.example.exceptions.ApiException;

import java.util.List;

public interface IDAO<T, K> {

    List<T> getAll() throws ApiException;

    T getById(K id) throws ApiException;

    T create(T in) throws ApiException;

    T update(T in, K id) throws ApiException;

    T delete(K id) throws ApiException;


}