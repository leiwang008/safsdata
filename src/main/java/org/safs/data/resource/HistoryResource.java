/**
 * Copyright (C) SAS Institute, All rights reserved.
 * General Public License: http://www.opensource.org/licenses/gpl-license.php
 */

/**
 * Logs for developers, not published to API DOC.
 *
 * History:
 * @date 2018-03-02    (Lei Wang) Initial release.
 * @date 2018-06-04    (Lei Wang) Removed field 'engineId', 'timestamp'; Added fields 'testName', 'beginTimestamp' and 'endTimestamp'.
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
	private String userId;
	private String testName;
	private Date beginTimestamp;
	private Date endTimestamp;
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
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	public String getTestName() {
		return testName;
	}
	/**
	 * @return the beginTimestamp
	 */
	public Date getBeginTimestamp() {
		return beginTimestamp;
	}

	/**
	 * @return the endTimestamp
	 */
	public Date getEndTimestamp() {
		return endTimestamp;
	}

	/**
	 * @return the commandLine
	 */
	public String getCommandLine() {
		return commandLine;
	}
}
