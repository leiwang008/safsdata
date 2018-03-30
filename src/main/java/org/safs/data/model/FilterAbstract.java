/**
 * Copyright (C) SAS Institute, All rights reserved.
 * General Public License: http://www.opensource.org/licenses/gpl-license.php
 */

/**
 * Logs for developers, not published to API DOC.
 *
 * History:
 * @date 2018-03-30    (Lei Wang) Initial release.
 */
package org.safs.data.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lei Wang
 *
 */
public abstract class FilterAbstract<T> implements Filter<T>{

	/**
	 * Pass each element to method {@link #shouldBeIgnored(Object)}, if it returns true then the element should be filtered.
	 */
	@Override
	public List<T> filter(List<T> elements) {
		List<T> filteredElements = new ArrayList<T>();

		for(T T:elements){
			if(!shouldBeIgnored(T)){
				filteredElements.add(T);
			}
		}

		return filteredElements;
	}

	/**
	 * @param element T, the element to test.
	 * @return boolean, true if the element should be ignored.
	 */
	public abstract boolean shouldBeIgnored(T element);
}
