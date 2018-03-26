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

import org.junit.Assert;
import org.junit.Test;
import org.safs.data.model.Machine;
import org.safs.data.model.Testcase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Lei Wang
 *
 */
public class ResourcesTest{
	private static final Logger log = LoggerFactory.getLogger(ResourcesTest.class);

	@Test
	public void testUpdateFromModel(){

		Machine m = new Machine();
		m.setId(123L);
		m.setIp("123.123.123.123");
		m.setName("testmachine");
		m.setPlatform("windows 10");

		log.debug(m.toString());
		Assert.assertEquals("123 | testmachine | 123.123.123.123 | windows 10 | ", m.toString());

		MachineResource mr = new MachineResource(m);
		log.debug(mr.toString());
		Assert.assertEquals(m.toString(), mr.toString());

		Testcase tc = new Testcase();
		tc.setId(123L);
		tc.setName("caseabc");
		tc.setTestsuiteId(12L);
		tc.setTime(123.5453);

		log.debug(tc.toString());
		Assert.assertEquals("123 | 12 | caseabc | 123.5453 | ", tc.toString());

		TestcaseResource tcr = new TestcaseResource(tc);
		log.debug(tcr.toString());
		//TestcaseResource's getTime() will round the double value and keep only 2 decimals
		Assert.assertEquals("123 | 12 | caseabc | 123.55 | ", tcr.toString());

	}
}
