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
package org.safs.data.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Lei Wang
 *
 */
@Entity
public class Teststep extends UpdatableDefault<Teststep>{
	/** 'teststeps' the base path to access entity */
	public static final String REST_BASE_PATH = "teststeps";
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Long testcaseId;
	private Long statusId;
	private String logMessage;
	/**
	 *
	 */
	public Teststep() {
		super();
	}

	/**
	 * @param testcaseId
	 * @param statusId
	 * @param logMessage
	 */
	public Teststep(Long testcaseId, Long statusId, String logMessage) {
		super();
		this.testcaseId = testcaseId;
		this.statusId = statusId;
		this.logMessage = logMessage;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the testcaseId
	 */
	public Long getTestcaseId() {
		return testcaseId;
	}

	/**
	 * @param testcaseId the testcaseId to set
	 */
	public void setTestcaseId(Long testcaseId) {
		this.testcaseId = testcaseId;
	}

	/**
	 * @return the statusId
	 */
	public Long getStatusId() {
		return statusId;
	}

	/**
	 * @param statusId the statusId to set
	 */
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	/**
	 * @return the logMessage
	 */
	public String getLogMessage() {
		return logMessage;
	}

	/**
	 * @param logMessage the logMessage to set
	 */
	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}

//	@Override
//	public void update(Teststep o) {
//		testcaseId = o.testcaseId;
//		statusId = o.statusId;
//		logMessage = o.logMessage;
//	}
//
//	@Override
//	public String toString(){
//		return id+" | " + testcaseId+ " | "+ statusId+" | "+logMessage;
//	}

}
