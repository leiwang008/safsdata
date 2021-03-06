/**
 * Copyright (C) SAS Institute, All rights reserved.
 * General Public License: http://www.opensource.org/licenses/gpl-license.php
 */

/**
 * Logs for developers, not published to API DOC.
 *
 * History:
 * @date 2018-03-02    (Lei Wang) Initial release.
 */
package org.safs.data.resource;

import org.safs.data.model.Framework;

/**
 * @author Lei Wang
 *
 */
public class FrameworkResource extends UpdatableDefaultForResource<Framework>{
	private Long id;

	private String name;
	private String version;
	private String description;

	public FrameworkResource(Framework o){
		update(o);
	}
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
}
