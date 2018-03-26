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
package org.safs.data.resource;

import org.safs.data.model.Updatable;
import org.safs.data.model.UpdatableDefault;

/**
 * This class overrides the method toString() to print each fields with a separator "|" between them, such as "tomroc | tom | rocker | ".<br>
 * This class implements the method update(), it will update all the fields by the value from an other Updatable object, except the field annotated with "@Id".<br>
 * The model entity can extend this class to get these functionalities.<br>
 *
 * @author Lei Wang
 */
public class UpdatableDefaultForResource<T extends Updatable<T>> extends UpdatableDefault<T>{
	//In case, we want a different implementation of method toString() and update() for resources.
}
