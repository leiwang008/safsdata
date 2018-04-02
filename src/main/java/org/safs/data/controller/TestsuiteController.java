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
import org.safs.data.model.ConstantPath;
import org.safs.data.model.Testcase;
import org.safs.data.model.Testcycle;
import org.safs.data.model.Testsuite;
import org.safs.data.repository.TestcaseRepository;
import org.safs.data.repository.TestcycleRepository;
import org.safs.data.repository.TestsuiteRepository;
import org.safs.data.resource.TestsuiteResource;
import org.safs.data.resource.TestsuiteResourceAssembler;
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
@ExposesResourceFor(Testsuite.class)
@RequestMapping(value=Testsuite.REST_BASE_PATH, produces=MediaType.APPLICATION_JSON_VALUE)
public class TestsuiteController implements Verifier<Testsuite> {
	private static final Logger log = LoggerFactory.getLogger(TestsuiteController.class);

	@Autowired
	TestcaseRepository testcaseRepository;
	@Autowired
	TestsuiteRepository testsuiteRepository;
	@Autowired
	TestcycleRepository testcycleRepository;

	@Autowired
	TestsuiteResourceAssembler assembler;

	@GetMapping
	public ResponseEntity<Collection<TestsuiteResource>> findAll(){
		Iterable<Testsuite> entities = testsuiteRepository.findAll();
		return new ResponseEntity<>( assembler.toResourceCollection(entities), HttpStatus.OK);
	}

	@PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TestsuiteResource> create(@RequestBody Testsuite body){
		verifyDependenciesExist(body);

		Testsuite testsuite = testsuiteRepository.save(body);
		log.debug(Testsuite.class.getName()+" has been created in the repository.");
		return new ResponseEntity<>(assembler.toResource(testsuite), HttpStatus.CREATED);
	}

	@GetMapping(value="{id}")
	public ResponseEntity<TestsuiteResource> find(@PathVariable Long id){
		Optional<Testsuite> enetity = testsuiteRepository.findById(id);
		try{
			return ResponseEntity.status(HttpStatus.OK).body(assembler.toResource(enetity.get()));
		}catch(NoSuchElementException nse){
			throw new RestException("Failed to find "+Testsuite.class.getName()+" by id '"+id+"'", HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value="{id}")
	public ResponseEntity<TestsuiteResource> delete(@PathVariable Long id){
		try{
			verifyDependentsNotExist(testsuiteRepository.findById(id).get());
			testsuiteRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}catch(NoSuchElementException e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@PutMapping(value="{id}", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TestsuiteResource> update(@PathVariable Long id, @RequestBody Testsuite body){
		verifyDependenciesExist(body);

		try{
			Testsuite original = testsuiteRepository.findById(id).get();
			original.update(body);
			testsuiteRepository.save(original);
			return ResponseEntity.status(HttpStatus.OK).body(assembler.toResource(original));
		}catch(NoSuchElementException e){
			throw new RestException("Failed to upate "+Testsuite.class.getName()+" by id '"+id+"'", HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public void verifyDependenciesExist(Testsuite entity) throws RestException {
		String me = entity.getClass().getName();
		String dependency = null;
		//It depends on 'Testcycle'
		try{
			dependency = Testcycle.class.getName();
			testcycleRepository.findById(entity.getTestcycleId()).get();
		}catch(NoSuchElementException e){
			throw new RestException("Cannot find "+dependency+" by ID '"+entity.getTestcycleId()+"', so the "+me+" cannot be updated to refer to that "+dependency+".");
		}
	}

	@Override
	public void verifyDependentsNotExist(Testsuite entity) throws RestException {
		String me = entity.getClass().getName();
		//'Testcase' depends on me.
		String dependent = Testcase.class.getName();
		if(!testcaseRepository.findAllBytestsuiteId(entity.getId()).isEmpty()){
			throw new RestException("Cannot delete "+me+" by id '"+entity.getId()+"', there are still "+dependent+"s depending on it!", HttpStatus.FAILED_DEPENDENCY);
		}
	}

	//================================== MVC controller: To be resolved by InternalResourceViewResolver to .jsp page ============================================
	@RequestMapping(value=ConstantPath.CHART_WITH_SEPARATOR, method=RequestMethod.GET)
	public String chart(ModelMap model){

		Collection<TestsuiteResource> elements = assembler.toResourceCollection(testsuiteRepository.findAll());

		model.put("elements", elements);
		log.debug("Got a collection: "+elements);

		return "testsuiteChart";//the view
	}
}
