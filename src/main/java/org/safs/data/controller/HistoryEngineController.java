/**
 * Copyright (C) SAS Institute, All rights reserved.
 * General Public License: http://www.opensource.org/licenses/gpl-license.php
 */

/**
 * Logs for developers, not published to API DOC.
 *
 * History:
 * @date 2018-03-22    (Lei Wang) Initial release.
 * @date 2018-04-04    (Lei Wang) Renamed method put() to create(); don't check id and always save it to database.
 */
package org.safs.data.controller;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.safs.data.exception.RestException;
import org.safs.data.model.Engine;
import org.safs.data.model.History;
import org.safs.data.model.HistoryEngine;
import org.safs.data.model.HistoryEngineID;
import org.safs.data.repository.EngineRepository;
import org.safs.data.repository.HistoryEngineRepository;
import org.safs.data.repository.HistoryRepository;
import org.safs.data.resource.HistoryEngineResource;
import org.safs.data.resource.HistoryEngineResourceAssembler;
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
@ExposesResourceFor(HistoryEngine.class)
@RequestMapping(value=HistoryEngine.REST_BASE_PATH, produces=MediaType.APPLICATION_JSON_VALUE)
public class HistoryEngineController implements Verifier<HistoryEngine>{
	private static final Logger log = LoggerFactory.getLogger(HistoryEngineController.class);

	@Autowired
	private HistoryEngineRepository historyEngineRepository;
	@Autowired
	private HistoryRepository historyRepository;
	@Autowired
	private EngineRepository engineRepository;

	@Autowired
	private HistoryEngineResourceAssembler assembler;

	//================================== REST APIs controller ============================================
	@GetMapping
	public ResponseEntity<Collection<HistoryEngineResource>> findAll(){
		Iterable<HistoryEngine> elements = historyEngineRepository.findAll();
		Collection<HistoryEngineResource> resources = assembler.toResourceCollection(elements);
		return new ResponseEntity<>(resources, HttpStatus.OK);
	}

	@PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HistoryEngineResource> create(@RequestBody HistoryEngine body){
		verifyDependenciesExist(body);

		HistoryEngine o = historyEngineRepository.save(body);
		log.debug(HistoryEngine.class.getName()+" has been created in the repository.");
		return new ResponseEntity<>(assembler.toResource(o), HttpStatus.CREATED);
	}

	@GetMapping(value="{historyIdEngineId}")
	public ResponseEntity<HistoryEngineResource> find(@PathVariable String historyIdEngineId/* historyId-EngineId*/){
		try{
			HistoryEngineID compositeId = new HistoryEngineID(historyIdEngineId);
			Optional<HistoryEngine> element = historyEngineRepository.findById(compositeId);
			return new ResponseEntity<>(assembler.toResource(element.get()), HttpStatus.OK);
		}catch(NoSuchElementException nse){
			throw new RestException("Failed to find "+HistoryEngine.class.getName()+" by id '"+historyIdEngineId+"'", HttpStatus.NOT_FOUND);
		}catch(IllegalArgumentException ile){
			throw new RestException(ile.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value="{historyIdEngineId}")
	public ResponseEntity<HistoryEngineResource> delete(@PathVariable String historyIdEngineId/* historyId-EngineId*/){
		try{
			HistoryEngineID compositeId = new HistoryEngineID(historyIdEngineId);
			verifyDependentsNotExist(historyEngineRepository.findById(compositeId).get());
			historyEngineRepository.deleteById(compositeId);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}catch(NoSuchElementException e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}catch(IllegalArgumentException ile){
			throw new RestException(ile.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(value="{historyIdEngineId}")
	public ResponseEntity<HistoryEngineResource> update(@PathVariable String historyIdEngineId/* historyId-EngineId*/, @RequestBody HistoryEngine body){
		HistoryEngineID compositeId = null;

		try{
			compositeId = new HistoryEngineID(historyIdEngineId);
		}catch(IllegalArgumentException ile){
			throw new RestException(ile.getMessage(), HttpStatus.NOT_FOUND);
		}

		if(!historyEngineRepository.existsById(compositeId)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}else{
			HistoryEngine original = historyEngineRepository.findById(compositeId).get();
			original.update(body);
			verifyDependenciesExist(original);
			historyEngineRepository.save(original);
			return new ResponseEntity<>(assembler.toResource(original), HttpStatus.OK);
		}
	}

	@Override
	public void verifyDependenciesExist(HistoryEngine entity) throws RestException {
		//I depend on History and Engine.
		String me = entity.getClass().getName();
		String dependency = null;
		try{
			dependency = History.class.getName();
			historyRepository.findById(entity.getHistoryId()).get();
		}catch(NoSuchElementException e){
			throw new RestException("Cannot find "+dependency+" by ID '"+entity.getHistoryId()+"', so the "+me+" cannot be updated to refer to that "+dependency+".");
		}
		try{
			dependency = Engine.class.getName();
			engineRepository.findById(entity.getEngineId()).get();
		}catch(NoSuchElementException e){
			throw new RestException("Cannot find "+dependency+" by ID '"+entity.getEngineId()+"', so the "+me+" cannot be updated to refer to that "+dependency+".");
		}
	}

	@Override
	public void verifyDependentsNotExist(HistoryEngine entity) throws RestException {
		//no one depends on me.
		//String me = entity.getClass().getName();
	}

}
