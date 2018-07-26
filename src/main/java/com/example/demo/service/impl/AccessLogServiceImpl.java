package com.example.demo.service.impl;

import com.example.demo.dao.AccessLogRepository;
import com.example.demo.entity.AccessLog;
import com.example.demo.service.AccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@Service
public class AccessLogServiceImpl implements AccessLogService {

	@Autowired
	public AccessLogRepository accessLogRepository;

	public List<AccessLog> findAll() {
		return accessLogRepository.findAll();
	}

	public Page<AccessLog> findAll(Pageable pageable) {
		return accessLogRepository.findAll(pageable);
	}

	public AccessLog findOne(Serializable id) {
		return accessLogRepository.findById((Long) id).get();
	}

	@Transactional
	public void save(AccessLog t) {
		accessLogRepository.save(t);
		
	}

	@Transactional
	public void delete(AccessLog t) {
		accessLogRepository.delete(t);
		
	}


}
