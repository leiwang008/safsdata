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
import org.safs.data.model.Engine;
import org.safs.data.repository.EngineRepository;
import org.safs.data.repository.UsageRepository;
import org.safs.data.resource.EngineResource;
import org.safs.data.resource.EngineResourceAssembler;
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
@ExposesResourceFor(Engine.class)
@RequestMapping(value=Engine.REST_BASE_PATH, produces=MediaType.APPLICATION_JSON_VALUE)
public class EngineController implements Verifier<Engine>{
	private static final Logger log = LoggerFactory.getLogger(EngineController.class);

	@Autowired
	private EngineRepository engineRepository;
	@Autowired
	private UsageRepository usageRepository;

	@Autowired
	private EngineResourceAssembler assembler;

	//================================== REST APIs controller ============================================
	@GetMapping
	public ResponseEntity<Collection<EngineResource>> findAll(){
		Iterable<Engine> elements = engineRepository.findAll();
		Collection<EngineResource> resources = assembler.toResourceCollection(elements);
		return new ResponseEntity<>(resources, HttpStatus.OK);
	}

	@PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EngineResource> put(@RequestBody Engine body){
		try{
			Optional<Engine> element = engineRepository.findById(body.getId());
			return new ResponseEntity<>(assembler.toResource(element.get()), HttpStatus.FOUND);
		}catch(NoSuchElementException nse){
			verifyDependenciesExist(body);
			Engine o = engineRepository.save(body);
			log.debug(Engine.class.getName()+" has been created in the repository.");
			return new ResponseEntity<>(assembler.toResource(o), HttpStatus.CREATED);
		}
	}

	@GetMapping(value="{id}")
	public ResponseEntity<EngineResource> find(@PathVariable Long id){
		try{
			Optional<Engine> element = engineRepository.findById(id);
			return new ResponseEntity<>(assembler.toResource(element.get()), HttpStatus.OK);
		}catch(NoSuchElementException nse){
			throw new RestException("Failed to find "+Engine.class.getName()+" by id '"+id+"'", HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value="{id}")
	public ResponseEntity<EngineResource> delete(@PathVariable Long id){
		try{
			verifyDependentsNotExist(engineRepository.findById(id).get());
			engineRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}catch(NoSuchElementException e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@PutMapping(value="{id}")
	public ResponseEntity<EngineResource> update(@PathVariable Long id, @RequestBody Engine body){
		if(!engineRepository.existsById(id)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}else{
			Engine original = engineRepository.findById(id).get();
			original.update(body);
			verifyDependenciesExist(original);
			engineRepository.save(original);
			return new ResponseEntity<>(assembler.toResource(original), HttpStatus.OK);
		}
	}

	@Override
	public void verifyDependenciesExist(Engine entity) throws RestException {
		//depends on nothing
	}

	@Override
	public void verifyDependentsNotExist(Engine entity) throws RestException {
		String me = entity.getClass().getName();
		//'Usage' depends on me.
		String dependent = Usage.class.getName();
		if(!usageRepository.findAllByMachineId(entity.getId()).isEmpty()){
			throw new RestException("Cannot delete "+me+" by id '"+entity.getId()+"', there are still "+dependent+"s depending on it!", HttpStatus.FAILED_DEPENDENCY);
		}
	}

}
