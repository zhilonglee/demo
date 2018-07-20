package com.example.demo.listener;

import com.example.demo.entity.AccessLog;
import com.example.demo.service.AccessLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class AccessEventListener implements ApplicationListener<AccessEvent> {

	private static final Logger logger = LoggerFactory.getLogger(AccessEventListener.class);
	
	@Autowired
	AccessLogService accessLogService;
	
	@Override
	public void onApplicationEvent(AccessEvent event) {
		try {
			AccessLog accesslog = new AccessLog();
			accesslog.setIpaddress(event.getIpAddress());
			accesslog.setUrl(event.getUrl());
			accessLogService.save(accesslog);
			logger.info(accesslog.toString());
		} catch (Exception e) {
			logger.error("",e);
		}
		
	}

}
