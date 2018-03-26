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

import org.safs.data.model.Teststep;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Lei Wang
 *
 */
@Repository
public interface TeststepRepository extends CrudRepository<Teststep, Long>{

	@Query("delete from Teststep i where i.testcaseId=:testcaseId")
	public boolean deleteByTestcaseId(@Param("testcaseId") Long testcaseId);
	@Query("delete from Teststep i where i.statusId=:statusId")
	public boolean deleteByStatusId(@Param("statusId") Long statusId);

	@Query("select i from Teststep i where i.testcaseId=:testcaseId")
	public List<Teststep> findAllByTestcaseId(@Param("testcaseId") Long testcaseId);
	@Query("select i from Teststep i where i.statusId=:statusId")
	public List<Teststep> findAllByStatusId(@Param("statusId") Long statusId);
}