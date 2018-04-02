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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.safs.data.exception.RestException;
import org.safs.data.model.ConstantPath;
import org.safs.data.model.Orderable;
import org.safs.data.model.Testcycle;
import org.safs.data.model.Testsuite;
import org.safs.data.repository.OrderableRepository;
import org.safs.data.repository.TestcycleRepository;
import org.safs.data.repository.TestsuiteRepository;
import org.safs.data.resource.TestcycleResource;
import org.safs.data.resource.TestcycleResourceAssembler;
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
@ExposesResourceFor(Testcycle.class)
@RequestMapping(value=Testcycle.REST_BASE_PATH, produces=MediaType.APPLICATION_JSON_VALUE)
public class TestcycleController implements Verifier<Testcycle> {
	private static final Logger log = LoggerFactory.getLogger(TestcycleController.class);

	@Autowired
	TestcycleRepository testcycleRepository;
	@Autowired
	TestsuiteRepository testsuiteRepository;
	@Autowired
	OrderableRepository orderableRepository;

	@Autowired
	TestcycleResourceAssembler assembler;

	@GetMapping
	public ResponseEntity<Collection<TestcycleResource>> findAll(){
		Iterable<Testcycle> entities = testcycleRepository.findAll();
		return new ResponseEntity<>( assembler.toResourceCollection(entities), HttpStatus.OK);
	}

	@PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TestcycleResource> create(@RequestBody Testcycle body){
		verifyDependenciesExist(body);

		Testcycle testcycle = testcycleRepository.save(body);
		log.debug(Testcycle.class.getName()+" has been created in the repository.");
		return new ResponseEntity<>(assembler.toResource(testcycle), HttpStatus.CREATED);
	}

	@GetMapping(value="{id}")
	public ResponseEntity<TestcycleResource> find(@PathVariable Long id){
		Optional<Testcycle> enetity = testcycleRepository.findById(id);
		try{
			return ResponseEntity.status(HttpStatus.OK).body(assembler.toResource(enetity.get()));
		}catch(NoSuchElementException nse){
			throw new RestException("Failed to find "+Testcycle.class.getName()+" by id '"+id+"'", HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value="{id}")
	public ResponseEntity<TestcycleResource> delete(@PathVariable Long id){
		try{
			verifyDependentsNotExist(testcycleRepository.findById(id).get());
			testcycleRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}catch(NoSuchElementException e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@PutMapping(value="{id}", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TestcycleResource> update(@PathVariable Long id, @RequestBody Testcycle body){
		verifyDependenciesExist(body);

		try{
			Testcycle original = testcycleRepository.findById(id).get();
			//keep the original testsuites to avoiding accidently clean up.
//			body.setTestsuites(original.getTestsuites());
			original.update(body);
			testcycleRepository.save(original);
			return ResponseEntity.status(HttpStatus.OK).body(assembler.toResource(original));
		}catch(NoSuchElementException e){
			throw new RestException("Failed to upate "+Testcycle.class.getName()+" by id '"+id+"'", HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public void verifyDependenciesExist(Testcycle entity) throws RestException {
		String me = entity.getClass().getName();
		String dependency = null;
		//It depends on 'Orderable'
		try{
			dependency = Orderable.class.getName();
			orderableRepository.findById(entity.getOrderableId()).get();
		}catch(NoSuchElementException e){
			throw new RestException("Cannot find "+dependency+" by ID '"+entity.getOrderableId()+"', so the "+me+" cannot be updated to refer to that "+dependency+".");
		}
	}

	@Override
	public void verifyDependentsNotExist(Testcycle entity) throws RestException {
		String me = entity.getClass().getName();
		//'Testsuite' depends on me.
		String dependent = Testsuite.class.getName();
		if(!testsuiteRepository.findAllByTestcycleId(entity.getId()).isEmpty()){
			throw new RestException("Cannot delete "+me+" by id '"+entity.getId()+"', there are still "+dependent+"s depending on it!", HttpStatus.FAILED_DEPENDENCY);
		}
	}

	//================================== MVC controller: To be resolved by InternalResourceViewResolver to .jsp page ============================================
	@RequestMapping(value=ConstantPath.CHART_WITH_SEPARATOR, method=RequestMethod.GET)
	public String chart(ModelMap model){

		Field[] fields = TestcycleResource.class.getDeclaredFields();
		List<String> fieldNames = new ArrayList<String>();
		for(Field field:fields){
			fieldNames.add(field.getName());
		}
		model.put("fieldNames", fieldNames);

		Collection<TestcycleResource> elements = assembler.toResourceCollection(testcycleRepository.findAll());
		model.put("elements", elements);
		log.debug("Got a collection: "+elements);

		return "testcycleChart";//the view
	}
}
