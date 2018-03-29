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

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Lei Wang
 *
 */
@Entity
public class Usage extends UpdatableDefault<Usage>{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Long machineId;
	private Long frameworkId;
	private Long engineId;
	private String userId;
	private Date timestamp;
	private String commandLine;

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
	 * @return the machineId
	 */
	public Long getMachineId() {
		return machineId;
	}
	/**
	 * @param machineId the machineId to set
	 */
	public void setMachineId(Long machineId) {
		this.machineId = machineId;
	}
	/**
	 * @return the frameworkId
	 */
	public Long getFrameworkId() {
		return frameworkId;
	}
	/**
	 * @param frameworkId the frameworkId to set
	 */
	public void setFrameworkId(Long frameworkId) {
		this.frameworkId = frameworkId;
	}
	/**
	 * @return the engineId
	 */
	public Long getEngineId() {
		return engineId;
	}
	/**
	 * @param engineId the engineId to set
	 */
	public void setEngineId(Long engineId) {
		this.engineId = engineId;
	}
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * @return the commandLine
	 */
	public String getCommandLine() {
		return commandLine;
	}
	/**
	 * @param commandLine the commandLine to set
	 */
	public void setCommandLine(String commandLine) {
		this.commandLine = commandLine;
	}
}
