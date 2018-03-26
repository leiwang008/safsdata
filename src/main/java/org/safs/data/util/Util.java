/**
 * Copyright (C) SAS Institute, All rights reserved.
 * General Public License: http://www.opensource.org/licenses/gpl-license.php
 */

/**
 * Logs for developers, not published to API DOC.
 *
 * History:
 * @date 2018-03-06    (Lei Wang) Initial release.
 */
package org.safs.data.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Lei Wang
 *
 */
public class Util {
	public final static DecimalFormat decimalFormat2 = new DecimalFormat(".##");

	public static Double keep2Decimal(Double d){
		return Double.parseDouble(decimalFormat2.format(d));
	}

	public static <T> Collection<T> toCollection(Iterable<T> iterable){
		Collection<T> collection = new ArrayList<T>();
		for(T e: iterable){
			collection.add(e);
		}
		return collection;
	}
}
