package com.samsung.cs.repository;

import org.springframework.data.domain.Page;

import com.samsung.cs.model.entity.Ztinv001;
import com.samsung.cs.model.entity.key.Ztinv001Id;

public interface Ztinv001CustomRepository {

	Page<Ztinv001> findAllById(Iterable<Ztinv001Id> ids);
}
