package com.example.demo.handler;

import com.example.demo.listener.AccessEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


@Component
public class AccessHandler {

	@Autowired
    ApplicationContext applicationContext;
	
	public void publish(String url,String ipAddress){
		applicationContext.publishEvent(new AccessEvent(this, url, ipAddress));
	}

}
