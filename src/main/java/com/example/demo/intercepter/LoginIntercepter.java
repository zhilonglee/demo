package com.example.demo.intercepter;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginIntercepter implements HandlerInterceptor {
   /**
    * The preHandle method is for handler interception, and as the name implies, it will be called before the Controller handles it,
    * The Interceptor Interceptor in SpringMVC is chained and can have multiple interceptors,
    * And then SpringMVC will execute one after another in the order that it's declared,
    * And all the preHandle methods in the Interceptor are invoked before the Controller method is invoked.
    * This Interceptor chain structure of SpringMVC can also be interrupted,
    * This interrupts by making the return value of the preHandle false, and the entire request ends when the return value of the preHandle is false.
    */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    /**
     * This method will only execute if the current Interceptor's preHandle method returns true.
     * PostHandle is used for processor interception. It is executed after processing by the processor, namely after the method call of the Controller.
     * But it will be executed before the DispatcherServlet renders the view, which means that in this method you can manipulate the ModelAndView.
     * The chained structure of this method goes in the opposite direction of normal access, that is, the Interceptor Interceptor that is declared first will be invoked later,
     * That's like the inside of the struts 2 interceptor implementation process is a bit like,
     * I'm just going to call the invoke method of ActionInvocation manually in the intercept method in Struts2,
     * The invoke method of invoking ActionInvocation in Struts2 is to invoke the next Interceptor or the action,
     * And then the content that you want to invoke before the Interceptor is written before you invoke the invoke, and the content that you want to invoke after the Interceptor is written after you invoke the invoke method.
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

   /**
    * This method is also executed only if the return value of the current corresponding Interceptor preHandle method is true.          
    * This method will render the view execution after the entire request is completed, namely the DispatcherServlet, which is mainly used to clean up resources.
    */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
