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

import javax.persistence.Id;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class overrides the method toString() to print each fields with a separator "|" between them, such as "tomroc | tom | rocker | ".<br>
 * This class implements the method update(), it will update all the fields by the value from an other Updatable object, except the field annotated with "@Id".<br>
 * The model entity can extend this class to get these functionalities.<br>
 *
 * @author Lei Wang
 */
public class UpdatableDefault<T extends Updatable<T>> extends ToStringDefault implements Updatable <T>{
	private static final Logger log = LoggerFactory.getLogger(ToStringDefault.class);

	@Override
	public void update(T o) {
		Field[] fields = getClass().getDeclaredFields();
		Field oField = null;

		for(Field field:fields){
			try {
				//We don't update the ID field
				if(!field.isAnnotationPresent(Id.class)){
					oField = o.getClass().getDeclaredField(field.getName());
					field.setAccessible(true);
					oField.setAccessible(true);
					field.set(this, oField.get(o));
				}
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				log.debug("Failed to update field '"+field.getName()+"'",e);
				try {
					PropertyUtils.setProperty(this, field.getName(), oField.get(o));
				} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException
						| IllegalArgumentException e1) {
					log.debug("Failed to update field '"+field.getName()+"'",e1);
				}
			}
		}
	}

}
