/**
 * Copyright (C) SAS Institute, All rights reserved.
 * General Public License: http://www.opensource.org/licenses/gpl-license.php
 */

/**
 * Logs for developers, not published to API DOC.
 *
 * History:
 * @date 2018-03-29    (Lei Wang) Initial release.
 */
package org.safs.data.model;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author Lei Wang
 *
 */
public class FieldFilterByModifiers extends FilterAbstract<Field>{

	/**
	 * The field modifiers to be ignored. It can be bit-or combination of {@link Modifier#fieldModifiers()}.<br>
	 * By default it is 0x00000000, which means nothing will be ignored.<br>
	 * @example
	 * {@link Modifier#PRIVATE} | {@link Modifier#STATIC}, then the static and private fields will be ignored.
	 *
	 */
	private int modifiers = 0x00000000;

	/**
	 * @param modifiers
	 */
	public FieldFilterByModifiers(int modifiers) {
		this.modifiers = modifiers;
	}

	@Override
	public boolean shouldBeIgnored(Field field) {
		try{
			//If one bit of 'modifiers' matches with the field's modifier, then the field is ignored.
			if((field.getModifiers() & modifiers)!=0){
				return true;
			}
		}catch(Exception e){

		}
		return false;
	}

}
