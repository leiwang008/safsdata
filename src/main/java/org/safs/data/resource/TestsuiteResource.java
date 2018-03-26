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

import org.safs.data.model.Testsuite;
import org.safs.data.util.Util;

/**
 * @author Lei Wang
 *
 */
public class TestsuiteResource extends UpdatableDefaultForResource<Testsuite>{
	private Long id;
	private String name;
	private int tests;
	private int failures;
	private int skipped;
	private double time;
	private Date timestamp;
	private String commandLine;

	public TestsuiteResource(Testsuite o){
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
		return Util.keep2Decimal(time);
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
