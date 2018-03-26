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
import org.springframework.stereotype.Component;

/**
 * @author Lei Wang
 *
 */
@Component
public class TestcaseResourceAssembler extends ResourceAssembler<Testcase, TestcaseResource> {

	@Override
	public TestcaseResource toResource(Testcase domainObject) {
		return new TestcaseResource(domainObject);
	}

}
