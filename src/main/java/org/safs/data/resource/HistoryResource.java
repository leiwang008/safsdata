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

import java.util.Date;

import org.safs.data.model.History;

/**
 * @author Lei Wang
 *
 */
public class HistoryResource extends UpdatableDefaultForResource<History>{
	private Long id;

	private Long machineId;
	private Long frameworkId;
	private Long engineId;
	private String userId;
	private Date timestamp;
	private String commandLine;

	public HistoryResource(History o){
		update(o);
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the machineId
	 */
	public Long getMachineId() {
		return machineId;
	}

	/**
	 * @return the frameworkId
	 */
	public Long getFrameworkId() {
		return frameworkId;
	}

	/**
	 * @return the engineId
	 */
	public Long getEngineId() {
		return engineId;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * @return the commandLine
	 */
	public String getCommandLine() {
		return commandLine;
	}
}
