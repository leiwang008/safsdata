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

import org.safs.data.model.Teststep;
import org.springframework.stereotype.Component;

/**
 * @author Lei Wang
 *
 */
@Component
public class TeststepResourceAssembler extends ResourceAssembler<Teststep, TeststepResource> {

	@Override
	public TeststepResource toResource(Teststep domainObject) {
		return new TeststepResource(domainObject);
	}

}
