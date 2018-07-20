package com.example.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

public interface BaseService<T> {

	List<T> findAll();
	Page<T> findAll(Pageable pageable);
	T findOne(Serializable id);
	@Transactional
	void save(T t);
	@Transactional
	void delete(T t);
//	Page<T> findAll(T t, Pageable pageable)  throws Exception ;
	
}
