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

import org.safs.data.model.HistoryEngine;
import org.safs.data.model.HistoryEngineID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Lei Wang
 *
 */
@Repository
public interface HistoryEngineRepository extends CrudRepository<HistoryEngine, HistoryEngineID>{
	@Query("delete from HistoryEngine i where i.historyId=:historyId")
	public boolean deleteByHistoryId(@Param("historyId") Long historyId);

	@Query("select i from HistoryEngine i where i.historyId=:historyId")
	public List<HistoryEngine> findAllByHistoryId(@Param("historyId") Long historyId);

	@Query("select i from HistoryEngine i where i.engineId=:engineId")
	public List<HistoryEngine> findAllByEngineId(@Param("engineId") Long engineId);
}