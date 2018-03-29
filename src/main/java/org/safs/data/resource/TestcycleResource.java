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

import org.safs.data.model.Testcycle;

/**
 * @author Lei Wang
 *
 */
public class TestcycleResource extends UpdatableDefaultForResource<Testcycle>{
	private Long id;
//	private List<Testsuite> testsuites;
	private Long orderableId;
	private String name;
	private int tests;
	private int failures;
	private int skipped;
	private double time;
	private Date timestamp;

	public TestcycleResource(Testcycle o){
		update(o);
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
//	/**
//	 * @return the testsuites
//	 */
//	public List<Testsuite> getTestsuites() {
//		return testsuites;
//	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the orderableId
	 */
	public Long getOrderableId() {
		return orderableId;
	}

	/**
	 * @return the tests
	 */
	public int getTests() {
		return tests;
	}

	/**
	 * @return the failures
	 */
	public int getFailures() {
		return failures;
	}

	/**
	 * @return the skipped
	 */
	public int getSkipped() {
		return skipped;
	}

	/**
	 * @return the time
	 */
	public double getTime() {
		return time;
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}
}
