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

import org.jboss.jandex.TypeTarget.Usage;
import org.safs.data.exception.RestException;
import org.safs.data.model.Framework;
import org.safs.data.repository.FrameworkRepository;
import org.safs.data.repository.UsageRepository;
import org.safs.data.resource.FrameworkResource;
import org.safs.data.resource.FrameworkResourceAssembler;
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
@ExposesResourceFor(Framework.class)
@RequestMapping(value=Framework.REST_BASE_PATH, produces=MediaType.APPLICATION_JSON_VALUE)
public class FrameworkController implements Verifier<Framework>{
	private static final Logger log = LoggerFactory.getLogger(FrameworkController.class);

	@Autowired
	private FrameworkRepository frameworkRepository;
	@Autowired
	private UsageRepository usageRepository;

	@Autowired
	private FrameworkResourceAssembler assembler;

	//================================== REST APIs controller ============================================
	@GetMapping
	public ResponseEntity<Collection<FrameworkResource>> findAll(){
		Iterable<Framework> elements = frameworkRepository.findAll();
		Collection<FrameworkResource> resources = assembler.toResourceCollection(elements);
		return new ResponseEntity<>(resources, HttpStatus.OK);
	}

	@PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FrameworkResource> put(@RequestBody Framework body){
		try{
			Optional<Framework> element = frameworkRepository.findById(body.getId());
			return new ResponseEntity<>(assembler.toResource(element.get()), HttpStatus.FOUND);
		}catch(NoSuchElementException nse){
			verifyDependenciesExist(body);
			Framework o = frameworkRepository.save(body);
			log.debug(Framework.class.getName()+" has been created in the repository.");
			return new ResponseEntity<>(assembler.toResource(o), HttpStatus.CREATED);
		}
	}

	@GetMapping(value="{id}")
	public ResponseEntity<FrameworkResource> find(@PathVariable Long id){
		try{
			Optional<Framework> element = frameworkRepository.findById(id);
			return new ResponseEntity<>(assembler.toResource(element.get()), HttpStatus.OK);
		}catch(NoSuchElementException nse){
			throw new RestException("Failed to find "+Framework.class.getName()+" by id '"+id+"'", HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value="{id}")
	public ResponseEntity<FrameworkResource> delete(@PathVariable Long id){
		try{
			verifyDependentsNotExist(frameworkRepository.findById(id).get());
			frameworkRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}catch(NoSuchElementException e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@PutMapping(value="{id}")
	public ResponseEntity<FrameworkResource> update(@PathVariable Long id, @RequestBody Framework body){
		if(!frameworkRepository.existsById(id)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}else{
			Framework original = frameworkRepository.findById(id).get();
			original.update(body);
			verifyDependenciesExist(original);
			frameworkRepository.save(original);
			return new ResponseEntity<>(assembler.toResource(original), HttpStatus.OK);
		}
	}

	@Override
	public void verifyDependenciesExist(Framework entity) throws RestException {
		//depends on nothing
	}

	@Override
	public void verifyDependentsNotExist(Framework entity) throws RestException {
		String me = entity.getClass().getName();
		//'Usage' depends on me.
		String dependent = Usage.class.getName();
		if(!usageRepository.findAllByMachineId(entity.getId()).isEmpty()){
			throw new RestException("Cannot delete "+me+" by id '"+entity.getId()+"', there are still "+dependent+"s depending on it!", HttpStatus.FAILED_DEPENDENCY);
		}
	}

}
