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

import org.safs.data.model.Testcycle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Lei Wang
 *
 */
@Repository
public interface TestcycleRepository extends CrudRepository<Testcycle, Long>{

}