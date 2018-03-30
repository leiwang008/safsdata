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
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lei Wang
 *
 */
public class FieldFilterComposite implements Filter<Field>{

	/**
	 * An aggregation of filters used to filter Field.
	 */
	private List<Filter<Field>> filters = new ArrayList<Filter<Field>>();

	/**
	 * @param names
	 */
	public FieldFilterComposite(Filter<Field> filter) {
		filters.add(filter);
	}

	public void addFilter(Filter<Field> filter){
		filters.add(filter);
	}

	@Override
	public List<Field> filter(List<Field> elements) {
		List<Field> leftElements = elements;
		for(Filter<Field> filter:filters){
			leftElements = filter.filter(leftElements);
		}
		return leftElements;
	}
}
