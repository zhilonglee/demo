package com.example.demo.dao;

import com.example.demo.entity.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, Long>,JpaSpecificationExecutor<Long> {

}
