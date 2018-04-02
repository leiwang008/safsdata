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

import org.safs.data.model.Usage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Lei Wang
 *
 */
@Repository
public interface UsageRepository extends CrudRepository<Usage, Long/*Id is the string type*/>{
	@Query("delete from Usage i where i.frameworkId=:frameworkId")
	public boolean deleteByFrameworkId(@Param("frameworkId") Long frameworkId);

	@Query("select i from Usage i where i.frameworkId=:frameworkId")
	public List<Usage> findAllByFrameworkId(@Param("frameworkId") Long frameworkId);

	@Query("delete from Usage i where i.engineId=:engineId")
	public boolean deleteByEngineId(@Param("engineId") Long engineId);

	@Query("select i from Usage i where i.engineId=:engineId")
	public List<Usage> findAllByEngineId(@Param("engineId") Long engineId);

	@Query("delete from Usage i where i.userId=:userId")
	public boolean deleteByUserId(@Param("userId") String userId);

	@Query("select i from Usage i where i.userId=:userId")
	public List<Usage> findAllByUserId(@Param("userId") String userId);

	@Query("delete from Usage i where i.machineId=:machineId")
	public boolean deleteByMachinekId(@Param("machineId") Long machineId);

	@Query("select i from Usage i where i.machineId=:machineId")
	public List<Usage> findAllByMachineId(@Param("machineId") Long machineId);

}
