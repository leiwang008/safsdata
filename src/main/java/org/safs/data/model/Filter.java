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

import java.util.List;

/**
 * @author Lei Wang
 */
public interface Filter <T> {
	/**
	 * Filter some elements from a Collection.<br>
	 * @param elements List<T>, the elements to filter
	 * @return List<T>, the left elements after being filtered.
	 */
	List<T> filter(List<T> elements);
}
