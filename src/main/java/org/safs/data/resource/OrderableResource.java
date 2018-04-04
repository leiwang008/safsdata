/**
 * Copyright (C) SAS Institute, All rights reserved.
 * General Public License: http://www.opensource.org/licenses/gpl-license.php
 */

/**
 * Logs for developers, not published to API DOC.
 *
 * History:
 * @date 2018-03-02    (Lei Wang) Initial release.
 * @date 2018-04-04    (Lei Wang) Change field name 'product_name' to 'productName': use camel format.
 */
package org.safs.data.resource;

import org.safs.data.model.Orderable;

/**
 * @author Lei Wang
 *
 */
public class OrderableResource extends UpdatableDefaultForResource<Orderable>{
	private Long id;

	private String productName;
	private String platform;
	private String track;
	private String branch;

	public OrderableResource(Orderable o){
		update(o);
	}
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}
	/**
	 * @return the platform
	 */
	public String getPlatform() {
		return platform;
	}
	/**
	 * @return the track
	 */
	public String getTrack() {
		return track;
	}
	/**
	 * @return the branch
	 */
	public String getBranch() {
		return branch;
	}
}
