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

import org.safs.data.model.Machine;

/**
 * @author Lei Wang
 *
 */
public class MachineResource extends UpdatableDefaultForResource<Machine>{
	private Long id;
	private String name;
	private String ip;
	private String platform;

	public MachineResource(Machine o){
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
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @return the platform
	 */
	public String getPlatform() {
		return platform;
	}
}
