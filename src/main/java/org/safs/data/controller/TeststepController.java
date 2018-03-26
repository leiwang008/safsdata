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
package org.safs.data.controller;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.safs.data.exception.RestException;
import org.safs.data.model.Teststep;
import org.safs.data.repository.StatusRepository;
import org.safs.data.repository.TestcaseRepository;
import org.safs.data.repository.TeststepRepository;
import org.safs.data.resource.TeststepResource;
import org.safs.data.resource.TeststepResourceAssembler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Lei Wang
 *
 */
@CrossOrigin(origins="*")
//@RestController
@Controller
//@ResponseBody
@ExposesResourceFor(Teststep.class)
@RequestMapping(value="/teststep", produces=MediaType.APPLICATION_JSON_VALUE)
public class TeststepController implements Verifier<Teststep>{
	private static final Logger log = LoggerFactory.getLogger(TeststepController.class);

	@Autowired
	TeststepRepository teststepRepository;
	@Autowired
	StatusRepository statusRepository;
	@Autowired
	TestcaseRepository testcaseRepository;

	@Autowired
	TeststepResourceAssembler assembler;

	@GetMapping
	public ResponseEntity<Collection<TeststepResource>> findAll(){
		Iterable<Teststep> entities = teststepRepository.findAll();
		return new ResponseEntity<>( assembler.toResourceCollection(entities), HttpStatus.OK);
	}

	@PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TeststepResource> create(@RequestBody Teststep body){

		verifyDependenciesExist(body);

		Teststep item = teststepRepository.save(body);
		log.debug("teststep has been created in the repository.");
		return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toResource(item));
	}

	@GetMapping(value="/{id}")
	public ResponseEntity<TeststepResource> find(@PathVariable Long id){
		Optional<Teststep> entity = teststepRepository.findById(id);
		try{
			return ResponseEntity.status(HttpStatus.OK).body(assembler.toResource(entity.get()));
		}catch(NoSuchElementException e){
			throw new RestException("Failed to find teststep by id '"+id+"'", HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value="/{id}")
	public ResponseEntity<TeststepResource> delete(@PathVariable Long id){
		if(teststepRepository.existsById(id)){
			teststepRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}else{
			throw new RestException("Failed to delete teststep by id '"+id+"'", HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(value="/{id}", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TeststepResource> update(@PathVariable Long id, @RequestBody Teststep body){
		verifyDependenciesExist(body);

		try{
			Teststep element = teststepRepository.findById(id).get();
			element.update(body);
			teststepRepository.save(element);
			return new ResponseEntity<>(assembler.toResource(element), HttpStatus.OK);
		}catch(NoSuchElementException e){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public void verifyDependenciesExist(Teststep entity) throws RestException {
		try{
			testcaseRepository.findById(entity.getTestcaseId()).get();
		}catch(NoSuchElementException e){
			throw new RestException("Cannot find testcase by ID '"+entity.getTestcaseId()+"', so the teststep cannot be updated to refer to that testcase.");
		}
		try{
			statusRepository.findById(entity.getStatusId()).get();
		}catch(NoSuchElementException e){
			throw new RestException("Cannot find status by ID '"+entity.getStatusId()+"', so the teststep cannot be updated to refer to that status.");
		}
	}

	@Override
	public void verifyDependentsNotExist(Teststep body) throws RestException {
		//No one depends on me
	}

	//================================== MVC controller: To be resolved by InternalResourceViewResolver to .jsp page ============================================
	@RequestMapping(value="/chart", method=RequestMethod.GET)
	public String chart(ModelMap model){

		Collection<TeststepResource> elements = assembler.toResourceCollection(teststepRepository.findAll());

		model.put("elements", elements);
		log.debug("Got a collection: "+elements);

		return "teststepChart";//the view
	}
}
