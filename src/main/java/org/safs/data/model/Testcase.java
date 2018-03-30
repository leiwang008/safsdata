/**
 * Copyright (C) SAS Institute, All rights reserved.
 * General Public License: http://www.opensource.org/licenses/gpl-license.php
 */

/**
 * Logs for developers, not published to API DOC.
 *
 * History:
 * @date 2018-03-05    (Lei Wang) Initial release.
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
public class Testcase extends UpdatableDefault<Testcase>{
	/** 'testcases' the base path to access entity */
	public static final String REST_BASE_PATH = "testcases";
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Long testsuiteId;
	private String name;
	private double time;

	public Testcase() {
		super();
	}
	/**
	 * @param testsuiteId
	 * @param name
	 * @param time
	 */
	public Testcase(Long testsuiteId, String name, double time) {
		super();
		this.testsuiteId = testsuiteId;
		this.name = name;
		this.time = time;
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
	 * @return the testsuiteId
	 */
	public Long getTestsuiteId() {
		return testsuiteId;
	}

	/**
	 * @param testsuiteId the testsuiteId to set
	 */
	public void setTestsuiteId(Long testsuiteId) {
		this.testsuiteId = testsuiteId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the time
	 */
	public double getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(double time) {
		this.time = time;
	}

//	@Override
//	public void update(Testcase o) {
//		testsuiteId = o.testsuiteId;
//		name = o.name;
//		time = o.time;
//	}
//
//    @Override
//	public String toString(){
//        return id+" | " + testsuiteId + " | " + name+ " | "+ Util.keep2Decimal(time);
//    }

}
