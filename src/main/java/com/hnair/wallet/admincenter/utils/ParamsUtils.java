package com.hnair.wallet.admincenter.utils;


import org.apache.commons.lang3.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 参数校验工具类
 * @author zhangxianbin
 *
 */
public class ParamsUtils {

    public static boolean checkParams(Object... params){
        for (Object param : params) {
        	if(param == null){
        		return false;
        	}
            if (param instanceof String && StringUtils.isBlank(param.toString())) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 获取对象空值属性集合
     * @param bean
     * @param excludedProNames
     * @return
     */
    public static Set<String> getBeanNullParams(Object bean,String... excludedProNames){
    	Map<String, Object> paramterMap = new HashMap<>();
    	getAllProperties(bean,paramterMap);
    	for(String excludedProName : excludedProNames){
    		paramterMap.remove(excludedProName);
    	}    	
		return paramterMap.keySet().stream().filter(key -> paramterMap.get(key) == null
				|| (paramterMap.get(key) instanceof String && paramterMap.get(key).toString().trim().length() == 0))
				.collect(Collectors.toSet());
    }

    /**
     * 将实体类 bean的所有属性放到paramterMap
     *
     * @param bean
     * @param paramterMap
     * @since 1.6+
     */
    @SuppressWarnings("rawtypes")
	public static <T> void getAllProperties(T bean, Map<String, Object> paramterMap) {
        //参数为实体对象
        Class type = bean.getClass();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(type);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean, new Object[0]);
                    paramterMap.put(propertyName, result);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("将 JavaBean : " + bean.getClass().getSimpleName() + " 转化为 Map失败！", e);
        }
    }
	
}
