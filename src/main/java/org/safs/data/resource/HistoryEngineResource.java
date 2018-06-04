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

import org.safs.data.model.HistoryEngine;

/**
 *
 * @author Lei Wang
 *
 */
public class HistoryEngineResource extends UpdatableDefaultForResource<HistoryEngine>{
	private Long historyId;
	private Long engineId;

	public HistoryEngineResource(HistoryEngine o) {
		update(o);
	}

	public Long getHistoryId() {
		return historyId;
	}
	public Long getEngineId() {
		return engineId;
	}
}
