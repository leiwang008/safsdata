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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.safs.data.exception.RestException;
import org.safs.data.model.Testcase;
import org.safs.data.model.Teststep;
import org.safs.data.repository.TestcaseRepository;
import org.safs.data.repository.TeststepRepository;
import org.safs.data.repository.TestsuiteRepository;
import org.safs.data.resource.TestcaseResource;
import org.safs.data.resource.TestcaseResourceAssembler;
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
@ExposesResourceFor(Testcase.class)
@RequestMapping(value="/testcases", produces=MediaType.APPLICATION_JSON_VALUE)
public class TestcaseController implements Verifier<Testcase>{
	private static final Logger log = LoggerFactory.getLogger(TestcaseController.class);

	@Autowired
	private TestcaseRepository testcaseRepository;
	@Autowired
	private TeststepRepository teststepRepository;
	@Autowired
	private TestsuiteRepository testsuiteRepository;

	@Autowired
	private TestcaseResourceAssembler assembler;

	@GetMapping
	public ResponseEntity<Collection<TestcaseResource>> findAll(){
		Iterable<Testcase> elements = testcaseRepository.findAll();
		Collection<TestcaseResource> f = assembler.toResourceCollection(elements);
		return new ResponseEntity<>(f, HttpStatus.OK);
	}

	@GetMapping(value="/testsuite/{suiteId}")
	public ResponseEntity<Collection<TestcaseResource>> findAllBytestsuiteId(@PathVariable Long suiteId){
		Collection<Testcase> c = testcaseRepository.findAllBytestsuiteId(suiteId);
		return new ResponseEntity<>(assembler.toResourceCollection(c), HttpStatus.OK);
	}

	@PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TestcaseResource> create(@RequestBody Testcase body){
		verifyDependenciesExist(body);

		Testcase cc = testcaseRepository.save(body);
		log.debug("testcase has been created in the repository.");
		return new ResponseEntity<>(assembler.toResource(cc), HttpStatus.CREATED);
	}

	@GetMapping(value="/{id}")
	public ResponseEntity<TestcaseResource> find(@PathVariable Long id){
		Optional<Testcase> c = testcaseRepository.findById(id);
		try{
			return new ResponseEntity<>(assembler.toResource(c.get()), HttpStatus.OK);
		}catch(NoSuchElementException e){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value="/{id}")
	public ResponseEntity<TestcaseResource> delete(@PathVariable Long id){
		try{
			verifyDependentsNotExist(testcaseRepository.findById(id).get());
			testcaseRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}catch(NoSuchElementException e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

	}

	@PutMapping(value="/{id}", consumes=MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<TestcaseResource> update(@PathVariable Long id, @RequestBody Testcase body){
		verifyDependenciesExist(body);

		try{
			Testcase element = testcaseRepository.findById(id).get();
			element.update(body);
			testcaseRepository.save(element);
			return new ResponseEntity<>(assembler.toResource(element), HttpStatus.OK);
		}catch(NoSuchElementException e){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public void verifyDependenciesExist(Testcase entity) throws RestException {
		try{
			testsuiteRepository.findById(entity.getTestsuiteId()).get();
		}catch(NoSuchElementException e){
			throw new RestException("Cannot find testsuite by ID '"+entity.getTestsuiteId()+"', so the testcase cannot be created with that testsuite.");
		}
	}

	@Override
	public void verifyDependentsNotExist(Testcase entity) throws RestException {
		List<Teststep> elements = teststepRepository.findAllByTestcaseId(entity.getId());
		if(!elements.isEmpty()){
			throw new RestException("Cannot delete testcase by id '"+entity.getId()+"', there are still teststeps depeding on it!", HttpStatus.FAILED_DEPENDENCY);
		}
	}

	//================================== MVC controller: To be resolved by InternalResourceViewResolver to .jsp page ============================================
	@RequestMapping(value="/chart", method=RequestMethod.GET)
	public String chart(ModelMap model){

		Collection<TestcaseResource> elements = assembler.toResourceCollection(testcaseRepository.findAll());

		model.put("elements", elements);
		log.debug("Got a collection: "+elements);

//		Map<String, Double> statistic = new HashMap<String, Double>();
//		String tempKey = null;
//		for(TestcaseResource c:elements){
//			tempKey = c.getTestsuiteId().toString();
//			if(statistic.containsKey(tempKey)){
//				statistic.put(tempKey, statistic.get(tempKey)+c.getTime());
//			}else{
//				statistic.put(tempKey, c.getTime());
//			}
//		}
//		model.put("statistic", statistic);

		return "testcaseChart";//the view
	}
}
