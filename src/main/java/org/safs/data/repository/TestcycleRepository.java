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
package org.safs.data.repository;

import java.util.List;

import org.safs.data.model.Testcycle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Lei Wang
 *
 */
@Repository
public interface TestcycleRepository extends CrudRepository<Testcycle, Long>{
	@Query("delete from Testcycle i where i.orderableId=:orderableId")
	public boolean deleteByOrderableId(@Param("orderableId") Long orderableId);

	@Query("select i from Testcycle i where i.orderableId=:orderableId")
	public List<Testcycle> findAllByOrderableId(@Param("orderableId") Long orderableId);
}