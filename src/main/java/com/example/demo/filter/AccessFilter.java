package com.example.demo.filter;

import com.example.demo.handler.AccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Component
@ServletComponentScan
@WebFilter(urlPatterns="/*",filterName="accessFilter")
public class AccessFilter implements Filter {

	@Autowired
	AccessHandler accessHandler;
	
	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request2 = (HttpServletRequest) request;
		accessHandler.publish(request2.getRequestURI(), request2.getRemoteAddr());
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

		
	}

}
