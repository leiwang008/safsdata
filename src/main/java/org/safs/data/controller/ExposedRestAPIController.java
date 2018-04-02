/**
 * Copyright (C) SAS Institute, All rights reserved.
 * General Public License: http://www.opensource.org/licenses/gpl-license.php
 */

/**
 * Logs for developers, not published to API DOC.
 *
 * History:
 * @date 2018-03-05    (Lei Wang) Initial release.
 */
package org.safs.data.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.safs.data.model.Engine;
import org.safs.data.model.Framework;
import org.safs.data.model.Machine;
import org.safs.data.model.Orderable;
import org.safs.data.model.Status;
import org.safs.data.model.Testcase;
import org.safs.data.model.Testcycle;
import org.safs.data.model.Teststep;
import org.safs.data.model.Testsuite;
import org.safs.data.model.Usage;
import org.safs.data.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lei Wang
 *
 */
@CrossOrigin(origins="*")
@RestController
@RequestMapping(produces=MediaType.APPLICATION_JSON_VALUE)
public class ExposedRestAPIController {

	@Autowired
	protected EntityLinks entityLinks;

	@GetMapping
	public ResponseEntity<Collection<ExposedRestAPI>> findAll(){
		List<ExposedRestAPI> exposedAPI = new ArrayList<ExposedRestAPI>();

		//For usage
		exposedAPI.add(new ExposedRestAPI(Machine.class.getSimpleName(), entityLinks.linkFor(Machine.class).withSelfRel().getHref()));
		exposedAPI.add(new ExposedRestAPI(User.class.getSimpleName(), entityLinks.linkFor(User.class).withSelfRel().getHref()));
		exposedAPI.add(new ExposedRestAPI(Usage.class.getSimpleName(), entityLinks.linkFor(Usage.class).withSelfRel().getHref()));
		exposedAPI.add(new ExposedRestAPI(Framework.class.getSimpleName(), entityLinks.linkFor(Framework.class).withSelfRel().getHref()));
		exposedAPI.add(new ExposedRestAPI(Engine.class.getSimpleName(), entityLinks.linkFor(Engine.class).withSelfRel().getHref()));

		//For result
		exposedAPI.add(new ExposedRestAPI(Orderable.class.getSimpleName(), entityLinks.linkFor(Orderable.class).withSelfRel().getHref()));
		exposedAPI.add(new ExposedRestAPI(Testcycle.class.getSimpleName(), entityLinks.linkFor(Testcycle.class).withSelfRel().getHref()));
		exposedAPI.add(new ExposedRestAPI(Testsuite.class.getSimpleName(), entityLinks.linkFor(Testsuite.class).withSelfRel().getHref()));
		exposedAPI.add(new ExposedRestAPI(Testcase.class.getSimpleName(), entityLinks.linkFor(Testcase.class).withSelfRel().getHref()));
		exposedAPI.add(new ExposedRestAPI(Teststep.class.getSimpleName(), entityLinks.linkFor(Teststep.class).withSelfRel().getHref()));
		exposedAPI.add(new ExposedRestAPI(Status.class.getSimpleName(), entityLinks.linkFor(Status.class).withSelfRel().getHref()));

		return new ResponseEntity<>(exposedAPI, HttpStatus.OK);
	}
}

class ExposedRestAPI{
	private String name;
	private String href;

	public ExposedRestAPI(String name, String href){
		this.name = name;
		this.href = href;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the href
	 */
	public String getHref() {
		return href;
	}

}
