/**
 * Copyright (C) SAS Institute, All rights reserved.
 * General Public License: http://www.opensource.org/licenses/gpl-license.php
 */

/**
 * Logs for developers, not published to API DOC.
 *
 * History:
 * @date 2018-03-02    (Lei Wang) Initial release.
 * @date 2018-06-04    (Lei Wang) Removed the method related to 'engineId', which has been removed from the model.
 */
package org.safs.data.repository;

import java.util.List;

import org.safs.data.model.History;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Lei Wang
 *
 */
@Repository
public interface HistoryRepository extends CrudRepository<History, Long>{
	@Query("delete from History i where i.frameworkId=:frameworkId")
	public boolean deleteByFrameworkId(@Param("frameworkId") Long frameworkId);

	@Query("select i from History i where i.frameworkId=:frameworkId")
	public List<History> findAllByFrameworkId(@Param("frameworkId") Long frameworkId);

	@Query("delete from History i where i.userId=:userId")
	public boolean deleteByUserId(@Param("userId") String userId);

	@Query("select i from History i where i.userId=:userId")
	public List<History> findAllByUserId(@Param("userId") String userId);

	@Query("delete from History i where i.machineId=:machineId")
	public boolean deleteByMachinekId(@Param("machineId") Long machineId);

	@Query("select i from History i where i.machineId=:machineId")
	public List<History> findAllByMachineId(@Param("machineId") Long machineId);

}
