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

import org.safs.data.model.Testsuite;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Lei Wang
 *
 */
@Repository
public interface TestsuiteRepository extends CrudRepository<Testsuite, Long>{
	@Query("delete from Testsuite i where i.testcycleId=:testcycleId")
	public boolean deleteByTestcycleId(@Param("testcycleId") Long testcycleId);

	@Query("select i from Testsuite i where i.testcycleId=:testcycleId")
	public List<Testsuite> findAllByTestcycleId(@Param("testcycleId") Long testcycleId);
}
