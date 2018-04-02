/**
 * Copyright (C) SAS Institute, All rights reserved.
 * General Public License: http://www.opensource.org/licenses/gpl-license.php
 */

/**
 * Logs for developers, not published to API DOC.
 *
 * History:
 * @date 2018-03-22    (Lei Wang) Initial release.
 */
package org.safs.data.controller;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.safs.data.exception.RestException;
import org.safs.data.model.Orderable;
import org.safs.data.model.Testcycle;
import org.safs.data.repository.OrderableRepository;
import org.safs.data.repository.TestcycleRepository;
import org.safs.data.resource.OrderableResource;
import org.safs.data.resource.OrderableResourceAssembler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lei Wang
 *
 */
@CrossOrigin(origins="*")
@RestController
@ExposesResourceFor(Orderable.class)
@RequestMapping(value=Orderable.REST_BASE_PATH, produces=MediaType.APPLICATION_JSON_VALUE)
public class OrderableController implements Verifier<Orderable>{
	private static final Logger log = LoggerFactory.getLogger(OrderableController.class);

	@Autowired
	private OrderableRepository orderableRepository;
	@Autowired
	private TestcycleRepository testcycleRepository;

	@Autowired
	private OrderableResourceAssembler assembler;

	//================================== REST APIs controller ============================================
	@GetMapping
	public ResponseEntity<Collection<OrderableResource>> findAll(){
		Iterable<Orderable> elements = orderableRepository.findAll();
		Collection<OrderableResource> resources = assembler.toResourceCollection(elements);
		return new ResponseEntity<>(resources, HttpStatus.OK);
	}

	@PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<OrderableResource> put(@RequestBody Orderable body){
		try{
			Optional<Orderable> element = orderableRepository.findById(body.getId());
			return new ResponseEntity<>(assembler.toResource(element.get()), HttpStatus.FOUND);
		}catch(NoSuchElementException nse){
			verifyDependenciesExist(body);
			Orderable o = orderableRepository.save(body);
			log.debug(Orderable.class.getName()+" has been created in the repository.");
			return new ResponseEntity<>(assembler.toResource(o), HttpStatus.CREATED);
		}
	}

	@GetMapping(value="{id}")
	public ResponseEntity<OrderableResource> find(@PathVariable Long id){
		try{
			Optional<Orderable> element = orderableRepository.findById(id);
			return new ResponseEntity<>(assembler.toResource(element.get()), HttpStatus.OK);
		}catch(NoSuchElementException nse){
			throw new RestException("Failed to find "+Orderable.class.getName()+" by id '"+id+"'", HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value="{id}")
	public ResponseEntity<OrderableResource> delete(@PathVariable Long id){
		try{
			verifyDependentsNotExist(orderableRepository.findById(id).get());
			orderableRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}catch(NoSuchElementException e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@PutMapping(value="{id}")
	public ResponseEntity<OrderableResource> update(@PathVariable Long id, @RequestBody Orderable body){
		if(!orderableRepository.existsById(id)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}else{
			Orderable original = orderableRepository.findById(id).get();
			original.update(body);
			verifyDependenciesExist(original);
			orderableRepository.save(original);
			return new ResponseEntity<>(assembler.toResource(original), HttpStatus.OK);
		}
	}

	@Override
	public void verifyDependenciesExist(Orderable entity) throws RestException {
		//depends on nothing
	}

	@Override
	public void verifyDependentsNotExist(Orderable entity) throws RestException {
		String me = entity.getClass().getName();
		//'Testcycle' depends on me.
		String dependent = Testcycle.class.getName();
		if(!testcycleRepository.findAllByOrderableId(entity.getId()).isEmpty()){
			throw new RestException("Cannot delete "+me+" by id '"+entity.getId()+"', there are still "+dependent+"s depending on it!", HttpStatus.FAILED_DEPENDENCY);
		}
	}

}
