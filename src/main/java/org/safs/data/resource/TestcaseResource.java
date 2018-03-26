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

import org.safs.data.model.Testcase;
import org.safs.data.util.Util;

/**
 * @author Lei Wang
 *
 */
public class TestcaseResource extends UpdatableDefaultForResource<Testcase>{
	private Long id;
	private Long testsuiteId;
	private String name;
	private double time;

	public TestcaseResource(Testcase o) {
		update(o);
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @return the testsuiteId
	 */
	public Long getTestsuiteId() {
		return testsuiteId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the time
	 */
	public double getTime() {
		return Util.keep2Decimal(time);
	}
}
