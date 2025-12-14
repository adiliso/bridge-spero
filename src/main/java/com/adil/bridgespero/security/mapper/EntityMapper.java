package com.adil.bridgespero.security.mapper;

public interface EntityMapper<D, E> {

    E toEntity(D dto);

    D toDto(E entity);
}
