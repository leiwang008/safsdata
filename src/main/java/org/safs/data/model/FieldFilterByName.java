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
import java.util.List;

/**
 * @author Lei Wang
 *
 */
public class FieldFilterByName extends FilterAbstract<Field>{

	/**
	 * A list of names for the filed that should be ignored.
	 */
	private List<String> names = null;

	/**
	 * @param names
	 */
	public FieldFilterByName(List<String> names) {
		super();
		this.names = names;
	}

	@Override
	public boolean shouldBeIgnored(Field element) {
		try{
			for(String name:names){
				if(name.equals(element.getName())){
					return true;
				}
			}
		}catch(Exception e){

		}
		return false;
	}

}
