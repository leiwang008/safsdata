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
 * @date 2018-06-04    (Lei Wang) Do not depend on Engine anymore; HistoryEngine depends on me.
 */
package org.safs.data.controller;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.catalina.User;
import org.safs.data.exception.RestException;
import org.safs.data.model.Framework;
import org.safs.data.model.History;
import org.safs.data.model.HistoryEngine;
import org.safs.data.model.Machine;
import org.safs.data.repository.FrameworkRepository;
import org.safs.data.repository.HistoryEngineRepository;
import org.safs.data.repository.HistoryRepository;
import org.safs.data.repository.MachineRepository;
import org.safs.data.repository.UserRepository;
import org.safs.data.resource.HistoryResource;
import org.safs.data.resource.HistoryResourceAssembler;
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
@ExposesResourceFor(History.class)
@RequestMapping(value=History.REST_BASE_PATH, produces=MediaType.APPLICATION_JSON_VALUE)
public class HistoryController implements Verifier<History>{
	private static final Logger log = LoggerFactory.getLogger(HistoryController.class);

	@Autowired
	private HistoryRepository historyRepository;
	@Autowired
	private HistoryEngineRepository historyEngineRepository;
	@Autowired
	private MachineRepository machineRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FrameworkRepository framewordRepository;

	@Autowired
	private HistoryResourceAssembler assembler;

	//================================== REST APIs controller ============================================
	@GetMapping
	public ResponseEntity<Collection<HistoryResource>> findAll(){
		Iterable<History> elements = historyRepository.findAll();
		Collection<HistoryResource> resources = assembler.toResourceCollection(elements);
		return new ResponseEntity<>(resources, HttpStatus.OK);
	}

	@PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HistoryResource> create(@RequestBody History body){
		verifyDependenciesExist(body);

		History o = historyRepository.save(body);
		log.debug(History.class.getName()+" has been created in the repository.");
		return new ResponseEntity<>(assembler.toResource(o), HttpStatus.CREATED);
	}

	@GetMapping(value="{id}")
	public ResponseEntity<HistoryResource> find(@PathVariable Long id){
		try{
			Optional<History> element = historyRepository.findById(id);
			return new ResponseEntity<>(assembler.toResource(element.get()), HttpStatus.OK);
		}catch(NoSuchElementException nse){
			throw new RestException("Failed to find "+History.class.getName()+" by id '"+id+"'", HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value="{id}")
	public ResponseEntity<HistoryResource> delete(@PathVariable Long id) throws RestException{
		try{
			verifyDependentsNotExist(historyRepository.findById(id).get());
		}catch(NoSuchElementException e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}catch(RestException re){
			if(!HttpStatus.FAILED_DEPENDENCY.equals(re.getHttpStatus())){
				throw re;
			}
			log.debug("Tried to delete dependent HistoryEngines by historyID '"+id+"'.");
			if(!historyEngineRepository.deleteByHistoryId(id)){
				throw new RestException("Cannot delete HistoryEngine by historyID '"+id+"'!", HttpStatus.FAILED_DEPENDENCY);
			}
		}

		historyRepository.deleteById(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PutMapping(value="{id}")
	public ResponseEntity<HistoryResource> update(@PathVariable Long id, @RequestBody History body){

		if(!historyRepository.existsById(id)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}else{
			History original = historyRepository.findById(id).get();
			original.update(body);
			verifyDependenciesExist(original);
			historyRepository.save(original);
			return new ResponseEntity<>(assembler.toResource(original), HttpStatus.OK);
		}
	}

	@Override
	public void verifyDependenciesExist(History entity) throws RestException {
		//I depend on user, machine and framework.
		String me = entity.getClass().getName();
		String dependency = null;
		try{
			dependency = User.class.getName();
			userRepository.findById(entity.getUserId()).get();
		}catch(NoSuchElementException e){
			throw new RestException("Cannot find "+dependency+" by ID '"+entity.getUserId()+"', so the "+me+" cannot be updated to refer to that "+dependency+".");
		}
		try{
			dependency = Machine.class.getName();
			machineRepository.findById(entity.getMachineId()).get();
		}catch(NoSuchElementException e){
			throw new RestException("Cannot find "+dependency+" by ID '"+entity.getMachineId()+"', so the "+me+" cannot be updated to refer to that "+dependency+".");
		}
		try{
			dependency = Framework.class.getName();
			framewordRepository.findById(entity.getFrameworkId()).get();
		}catch(NoSuchElementException e){
			throw new RestException("Cannot find "+dependency+" by ID '"+entity.getFrameworkId()+"', so the "+me+" cannot be updated to refer to that "+dependency+".");
		}
	}

	@Override
	public void verifyDependentsNotExist(History entity) throws RestException {
		String me = entity.getClass().getName();
		//'HistoryEngine' depends on me.
		String dependent = HistoryEngine.class.getName();
		if(!historyEngineRepository.findAllByHistoryId(entity.getId()).isEmpty()){
			throw new RestException("There are still "+dependent+"s depending on '"+me+"' "+" indentified by id '"+entity.getId()+"!", HttpStatus.FAILED_DEPENDENCY);
		}
	}

}
