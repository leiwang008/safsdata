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
public class Testcycle extends UpdatableDefault<Testcycle>{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
//	//mappedBy="testcycle" will create a 'testcycle_id' references testcycle (id) in the Testsuite table.
//	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, mappedBy="testcycle")
//	private List<Testsuite> testsuites;

	private String name;
	private int tests;
	private int failures;
	private int skipped;
	private double time;
	private Date timestamp;

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
	 * @return the tests
	 */
	public int getTests() {
		return tests;
	}
	/**
	 * @param tests the tests to set
	 */
	public void setTests(int tests) {
		this.tests = tests;
	}
	/**
	 * @return the failures
	 */
	public int getFailures() {
		return failures;
	}
	/**
	 * @param failures the failures to set
	 */
	public void setFailures(int failures) {
		this.failures = failures;
	}
	/**
	 * @return the skipped
	 */
	public int getSkipped() {
		return skipped;
	}
	/**
	 * @param skipped the skipped to set
	 */
	public void setSkipped(int skipped) {
		this.skipped = skipped;
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

//	/**
//	 * @return the testsuites
//	 */
//	public List<Testsuite> getTestsuites() {
//		return testsuites;
//	}
//	/**
//	 * @param testsuites the testsuites to set
//	 */
//	public void setTestsuites(List<Testsuite> testsuites) {
//		this.testsuites = testsuites;
//	}

}
