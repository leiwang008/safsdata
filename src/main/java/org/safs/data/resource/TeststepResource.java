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
package org.safs.data.resource;

import org.safs.data.model.Teststep;

/**
 * @author Lei Wang
 *
 */
public class TeststepResource extends UpdatableDefaultForResource<Teststep>{
	private Long id;
	private Long testcaseId;
	private Long statusId;
	private String logMessage;

	public TeststepResource(Teststep o){
		update(o);
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the testcaseId
	 */
	public Long getTestcaseId() {
		return testcaseId;
	}

	/**
	 * @return the statusId
	 */
	public Long getStatusId() {
		return statusId;
	}

	/**
	 * @return the logMessage
	 */
	public String getLogMessage() {
		return logMessage;
	}
}
