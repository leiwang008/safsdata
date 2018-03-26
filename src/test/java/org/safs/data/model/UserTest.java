/**
 * Copyright (C) SAS Institute, All rights reserved.
 * General Public License: http://www.opensource.org/licenses/gpl-license.php
 */

/**
 * Logs for developers, not published to API DOC.
 *
 * History:
 * @date 2018-03-23    (Lei Wang) Initial release.
 */
package org.safs.data.model;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Lei Wang
 *
 */
public class UserTest{
	private static final Logger log = LoggerFactory.getLogger(UserTest.class);

	@Test
	public void testToStringAndUpdate(){
		User user1 = new User();
		user1.setId("tomroc");
		user1.setFirstName("tom");
		user1.setLastName("rocker");

		log.debug(user1.toString());
		Assert.assertEquals("tomroc | tom | rocker | ", user1.toString());

		User user2 = new User();
		user2.setId("jenchr");
		user2.setFirstName("jean");
		user2.setLastName("christ");

		log.debug(user2.toString());
		Assert.assertEquals("jenchr | jean | christ | ", user2.toString());

		user1.update(user2);
		log.debug(user1.toString());
		Assert.assertEquals("tomroc | jean | christ | ", user1.toString());
	}
}

