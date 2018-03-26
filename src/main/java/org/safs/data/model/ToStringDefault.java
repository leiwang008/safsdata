/**
 * Copyright (C) SAS Institute, All rights reserved.
 * General Public License: http://www.opensource.org/licenses/gpl-license.php
 */

/**
 * Logs for developers, not published to API DOC.
 *
 * History:
 * @date 2018-03-23    (Lei Wang) Initial release.
 */
package org.safs.data.model;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * This class overrides the method toString() to print each fields with a separator "|" between them, such as "tomroc | tom | rocker | ".<br>
 *
 * @author Lei Wang
 */
public class ToStringDefault{
	private static final Logger log = LoggerFactory.getLogger(ToStringDefault.class);

	@Override
	public String toString(){
		Field[] fields = getClass().getDeclaredFields();
		StringBuilder sb = new StringBuilder();

		for(Field field:fields) sb.append(getFieldValue(field)+" | ");

		return sb.toString();
	}

	/**
	 * Get the field's value. Firstly try to get it with getter method.
	 * @param field Field
	 * @return Object, the field's value, it can be null.
	 */
	private Object getFieldValue(Field field){
		Object value = null;
//		PropertyDescriptor pd;
//		try {
//			//to new a PropertyDescriptor, you MUST have getter and setter method in the class,
//			//otherwise it will throw out java.beans.IntrospectionException: Method not found: setId
//			pd = new PropertyDescriptor (field.getName(), getClass());
//			Method getterMethod = pd.getReadMethod();
//			if(pd!=null){
//				try {
//					value = getterMethod.invoke(this);
//				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//					log.debug("Failed to get value for field '"+field.getName()+"'",e);
//					field.setAccessible(true);
//					try {
//						value = field.get(this);
//					} catch (IllegalArgumentException | IllegalAccessException e1) {
//						log.debug("Failed to get value for field '"+field.getName()+"'",e1);
//					}
//				}
//			}
//		} catch (IntrospectionException e) {
//			log.debug("Failed to get value for field '"+field.getName()+"'",e);
//		}
		try {
			value = PropertyUtils.getProperty(this, field.getName());
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			log.debug("Failed to get value for field '"+field.getName()+"'",e);
			field.setAccessible(true);
			try {
				value = field.get(this);
			} catch (IllegalArgumentException | IllegalAccessException e1) {
				log.debug("Failed to get value for field '"+field.getName()+"'",e1);
			}
		}

		return value;
	}
}
