package com.example.demo.filter;

import com.example.demo.entity.Person;
import com.example.demo.handler.AccessHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
@ServletComponentScan
@WebFilter(urlPatterns="/*",filterName="accessFilter")
public class AccessFilter implements Filter {

	private final Logger logger = LoggerFactory.getLogger(AccessFilter.class);

	@Autowired
	AccessHandler accessHandler;
	
	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request2 = (HttpServletRequest) request;
		String requestURI = request2.getRequestURI();
		accessHandler.publish(requestURI, request2.getRemoteAddr());
/*		if(requestURI.contains("/v1/train/rabbit/train/")) {
			Person person = (Person) request2.getSession().getAttribute("person");
			if (person == null) {
				response.getWriter().print("redirect!");
				request2.getRequestDispatcher("/userlogin").forward(request,response);
			}else{
				chain.doFilter(request, response);
			}
		}else {
			chain.doFilter(request, response);
		}*/
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

		
	}

}
