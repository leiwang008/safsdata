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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.safs.data.exception.RestException;
import org.safs.data.model.Status;
import org.safs.data.model.Teststep;
import org.safs.data.repository.StatusRepository;
import org.safs.data.repository.TeststepRepository;
import org.safs.data.resource.StatusResource;
import org.safs.data.resource.StatusResourceAssembler;
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
@ExposesResourceFor(Status.class)
//If we don't use below RequestMapping, EntityLinks will not consider "/status" as link for class 'Status'
@RequestMapping(value="/statuses", produces=MediaType.APPLICATION_JSON_VALUE)
public class StatusController implements Verifier<Status> {
	private static final Logger log = LoggerFactory.getLogger(StatusController.class);

	@Autowired
	private StatusRepository statusRepository;
	@Autowired
	private TeststepRepository teststepRepository;

	@Autowired
	private StatusResourceAssembler assembler;

	//================================== REST APIs controller ============================================
	@GetMapping
	public ResponseEntity<Collection<StatusResource>> findAll(){
		Iterable<Status> elements = statusRepository.findAll();
		Collection<StatusResource> f = assembler.toResourceCollection(elements);
		return new ResponseEntity<>(f, HttpStatus.OK);
	}

	@PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StatusResource> create(@RequestBody Status body){
		Status status = statusRepository.save(body);
		log.debug("status has been created in the repository.");
		return new ResponseEntity<>(assembler.toResource(status), HttpStatus.CREATED);
	}

	@GetMapping(value="/{id}")
	public ResponseEntity<StatusResource> find(@PathVariable Long id){
		Optional<Status> element = statusRepository.findById(id);
		try{
			return new ResponseEntity<>(assembler.toResource(element.get()), HttpStatus.OK);
		}catch(NoSuchElementException nse){
			throw new RestException("Failed to find Status by id '"+id+"'", HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value="/{id}")
	public ResponseEntity<StatusResource> delete(@PathVariable Long id){
		try{
			verifyDependentsNotExist(statusRepository.findById(id).get());
			statusRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}catch(NoSuchElementException e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@PutMapping(value="/{id}")
	public ResponseEntity<StatusResource> update(@PathVariable Long id, @RequestBody Status body){
		if(!statusRepository.existsById(id)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}else{
			Status original = statusRepository.findById(id).get();
			original.update(body);
			statusRepository.save(original);
			return new ResponseEntity<>(assembler.toResource(original), HttpStatus.OK);
		}
	}

	@Override
	public void verifyDependenciesExist(Status entity) throws RestException {
		//Depend on nothing
	}

	@Override
	public void verifyDependentsNotExist(Status entity) throws RestException {
		List<Teststep> steps = teststepRepository.findAllByStatusId(entity.getId());
		if(!steps.isEmpty()){
			throw new RestException("Cannot delete status by id '"+entity.getId()+"', there are still Steps depeding on it!", HttpStatus.FAILED_DEPENDENCY);
		}
	}

	//================================== MVC controller: To be resolved by InternalResourceViewResolver to .jsp page ============================================
	@RequestMapping(value="/chart", method=RequestMethod.GET)
	public String statusChart(ModelMap model){

		Collection<StatusResource> elements = assembler.toResourceCollection(statusRepository.findAll());

		model.put("elements", elements);
		log.debug("Got a collection of status: "+elements);

		Map<String, Integer> statusStatistic = new HashMap<String, Integer>();
		String tempKey = null;
		for(StatusResource c:elements){
//			tempKey = c.getType();
			tempKey = c.getDescription();
			if(statusStatistic.containsKey(tempKey)){
				statusStatistic.put(tempKey, statusStatistic.get(tempKey)+1);
			}else{
				statusStatistic.put(tempKey, 1);
			}
		}
		model.put("statusStatistic", statusStatistic);

		return "statusChart";//statusChart.jsp is the view
	}
}
