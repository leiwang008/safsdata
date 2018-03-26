/**
 * Copyright (C) SAS Institute, All rights reserved.
 * General Public License: http://www.opensource.org/licenses/gpl-license.php
 */

/**
 * Logs for developers, not published to API DOC.
 *
 * History:
 * @date 2018-03-15    (Lei Wang) Initial release.
 */
package org.safs.data.model;

/**
 * Model class will implement this interface to update itself.
 *
 * @author Lei Wang
 *
 */
public interface Updatable <T>{
	public void update(T o);
}
