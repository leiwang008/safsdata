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
package org.safs.data.repository;

import java.util.List;

import org.safs.data.model.Orderable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Lei Wang
 *
 */
@Repository
public interface OrderableRepository extends CrudRepository<Orderable, Long>{
	//find Orderable by quadruplet("productName", "platform", "track", "branch")
	public List<Orderable> findByProductNameAndPlatformAndTrackAndBranch(String productName, String platform, String track, String branch);
}
