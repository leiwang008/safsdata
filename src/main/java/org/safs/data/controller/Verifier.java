/**
 * Copyright (C) SAS Institute, All rights reserved.
 * General Public License: http://www.opensource.org/licenses/gpl-license.php
 */

/**
 * Logs for developers, not published to API DOC.
 *
 * History:
 * @date 2018-03-16    (Lei Wang) Initial release.
 */
package org.safs.data.controller;

import org.safs.data.exception.RestException;

/**
 * @author Lei Wang
 *
 */
public interface Verifier<T>{
	/**
	 * This is normally called within method create() or update() to make suer that all dependencies exist.<br/>
	 * @param entity T
	 * @throws RestException if one of dependencies do not exist
	 */
	void verifyDependenciesExist(T entity) throws RestException;

	/**
	 * This is normally called within method delete() to make sure that all dependents do not exist.<br/>
	 * @param entity T
	 * @throws RestException if one of dependencies do not exist
	 */
	void verifyDependentsNotExist(T entity) throws RestException;
}
