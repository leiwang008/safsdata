/**
 * Copyright (C) SAS Institute, All rights reserved.
 * General Public License: http://www.opensource.org/licenses/gpl-license.php
 */

/**
 * Logs for developers, not published to API DOC.
 *
 * History:
 * @date 2018-03-06    (Lei Wang) Initial release.
 * @date 2018-03-29    (Lei Wang) Modified the path to plural (teststep -> teststeps).
 *                                Added more get request matching "/teststeps/testcase/{id}",
 *                                "/teststeps/status/{id}" and "/teststeps/testcase/{tcId}/status/{sId}".
 */
package org.safs.data.controller;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.safs.data.exception.RestException;
import org.safs.data.model.ConstantPath;
import org.safs.data.model.Status;
import org.safs.data.model.Testcase;
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
@RequestMapping(value=Teststep.REST_BASE_PATH, produces=MediaType.APPLICATION_JSON_VALUE)
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

	@GetMapping(value="{id}")
	public ResponseEntity<TeststepResource> find(@PathVariable Long id){
		Optional<Teststep> entity = teststepRepository.findById(id);
		try{
			return ResponseEntity.status(HttpStatus.OK).body(assembler.toResource(entity.get()));
		}catch(NoSuchElementException e){
			throw new RestException("Failed to find "+Teststep.class.getName()+" by id '"+id+"'", HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping
	public ResponseEntity<Collection<TeststepResource>> findAll(){
		Iterable<Teststep> entities = teststepRepository.findAll();
		return new ResponseEntity<>( assembler.toResourceCollection(entities), HttpStatus.OK);
	}

	/**
	 * Get all the teststeps according to the testcase's id.
	 * @param id Long, the test case's id.
	 * @return
	 */
	@GetMapping(value="testcase/{id}")
	public ResponseEntity<Collection<TeststepResource>> findAllByTestcaseId(@PathVariable Long id){
		Iterable<Teststep> entities = teststepRepository.findAllByTestcaseId(id);
		return new ResponseEntity<>( assembler.toResourceCollection(entities), HttpStatus.OK);
	}

	/**
	 * Get all the teststeps according to the status id.
	 * @param id Long, the status id.
	 * @return
	 */
	@GetMapping(value="status/{id}")
	public ResponseEntity<Collection<TeststepResource>> findAllByStatusId(@PathVariable Long id){
		Iterable<Teststep> entities = teststepRepository.findAllByStatusId(id);
		return new ResponseEntity<>( assembler.toResourceCollection(entities), HttpStatus.OK);
	}

	/**
	 * Get all the teststeps according to the testcase's id and status id.
	 * @param tcId Long, the test case's id.
	 * @param sId Long, the status id.
	 * @return
	 */
	@GetMapping(value="testcase/{tcId}/status/{sId}")
	public ResponseEntity<Collection<TeststepResource>> findAllByTestcaseId(@PathVariable Long tcId, @PathVariable Long sId){
		Iterable<Teststep> entities = teststepRepository.findAllByTestcaseIdAndStatusId(tcId, sId);
		return new ResponseEntity<>( assembler.toResourceCollection(entities), HttpStatus.OK);
	}

	@PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TeststepResource> create(@RequestBody Teststep body){

		verifyDependenciesExist(body);

		Teststep item = teststepRepository.save(body);
		log.debug(Teststep.class.getName()+" has been created in the repository.");
		return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toResource(item));
	}

	@DeleteMapping(value="{id}")
	public ResponseEntity<TeststepResource> delete(@PathVariable Long id){
		if(teststepRepository.existsById(id)){
			teststepRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}else{
			throw new RestException("Failed to delete "+Teststep.class.getName()+" by id '"+id+"'", HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(value="{id}", consumes=MediaType.APPLICATION_JSON_VALUE)
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
		String me = entity.getClass().getName();
		String dependency = null;
		//It depends on 'Testcase'
		try{
			dependency = Testcase.class.getName();
			testcaseRepository.findById(entity.getTestcaseId()).get();
		}catch(NoSuchElementException e){
			throw new RestException("Cannot find "+dependency+" by ID '"+entity.getTestcaseId()+"', so the "+me+" cannot be updated to refer to that "+dependency+".");
		}
		//It depends on 'Status'
		try{
			dependency = Status.class.getName();
			statusRepository.findById(entity.getStatusId()).get();
		}catch(NoSuchElementException e){
			throw new RestException("Cannot find "+dependency+" by ID '"+entity.getStatusId()+"', so the "+me+" cannot be updated to refer to that "+dependency+".");
		}
	}

	@Override
	public void verifyDependentsNotExist(Teststep body) throws RestException {
		//No one depends on me
	}

	//================================== MVC controller: To be resolved by InternalResourceViewResolver to .jsp page ============================================
	@RequestMapping(value=ConstantPath.CHART, method=RequestMethod.GET)
	public String chart(ModelMap model){

		Collection<TeststepResource> elements = assembler.toResourceCollection(teststepRepository.findAll());

		model.put("elements", elements);
		log.debug("Got a collection: "+elements);

		return "teststepChart";//the view
	}
}
