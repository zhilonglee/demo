package com.example.demo.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class EntityUtils<T> {
	
	public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static void copyPropertiesIgnoreNull(Object src, Object target){
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }
   
    public static <T> void getMethodName(T t){
    	
 //   	Class<?> clazz = root.getModel().getJavaType();
    	Class<?> clazz = t.getClass();
    	Method[] methods = clazz.getDeclaredMethods();
    	for (Method method : methods) {
    		if(method.getName().contains("set")){
    			String methodName = method.getName();
    			Field[] fields = clazz.getFields();
    			for (Field field : fields) {
					if(methodName.toLowerCase().contains(field.getName().toLowerCase())){
						field.setAccessible(true);
						try {
							System.out.println("Field : " + field.getName() + "& Method : " + methodName);
							Object val = field.get(t);
							if(val instanceof String){
								if(StringUtils.isNotEmpty((String)val)){
									System.out.println("Field val : " +val);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					}
				}
    		}
		}
    	
    }


}
