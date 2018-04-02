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

import org.apache.catalina.User;
import org.safs.data.exception.RestException;
import org.safs.data.model.Engine;
import org.safs.data.model.Framework;
import org.safs.data.model.Machine;
import org.safs.data.model.Usage;
import org.safs.data.repository.EngineRepository;
import org.safs.data.repository.FrameworkRepository;
import org.safs.data.repository.MachineRepository;
import org.safs.data.repository.UsageRepository;
import org.safs.data.repository.UserRepository;
import org.safs.data.resource.UsageResource;
import org.safs.data.resource.UsageResourceAssembler;
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
@ExposesResourceFor(Usage.class)
@RequestMapping(value=Usage.REST_BASE_PATH, produces=MediaType.APPLICATION_JSON_VALUE)
public class UsageController implements Verifier<Usage>{
	private static final Logger log = LoggerFactory.getLogger(UsageController.class);

	@Autowired
	private UsageRepository usageRepository;
	@Autowired
	private MachineRepository machineRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FrameworkRepository framewordRepository;
	@Autowired
	private EngineRepository engineRepository;

	@Autowired
	private UsageResourceAssembler assembler;

	//================================== REST APIs controller ============================================
	@GetMapping
	public ResponseEntity<Collection<UsageResource>> findAll(){
		Iterable<Usage> elements = usageRepository.findAll();
		Collection<UsageResource> resources = assembler.toResourceCollection(elements);
		return new ResponseEntity<>(resources, HttpStatus.OK);
	}

	@PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UsageResource> put(@RequestBody Usage body){
		verifyDependenciesExist(body);

		try{
			Optional<Usage> element = usageRepository.findById(body.getId());
			return new ResponseEntity<>(assembler.toResource(element.get()), HttpStatus.FOUND);
		}catch(NoSuchElementException nse){
			Usage o = usageRepository.save(body);
			log.debug(Usage.class.getName()+" has been created in the repository.");
			return new ResponseEntity<>(assembler.toResource(o), HttpStatus.CREATED);
		}
	}

	@GetMapping(value="{id}")
	public ResponseEntity<UsageResource> find(@PathVariable Long id){
		try{
			Optional<Usage> element = usageRepository.findById(id);
			return new ResponseEntity<>(assembler.toResource(element.get()), HttpStatus.OK);
		}catch(NoSuchElementException nse){
			throw new RestException("Failed to find "+Usage.class.getName()+" by id '"+id+"'", HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value="{id}")
	public ResponseEntity<UsageResource> delete(@PathVariable Long id){
		try{
			verifyDependentsNotExist(usageRepository.findById(id).get());
			usageRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}catch(NoSuchElementException e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@PutMapping(value="{id}")
	public ResponseEntity<UsageResource> update(@PathVariable Long id, @RequestBody Usage body){

		if(!usageRepository.existsById(id)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}else{
			Usage original = usageRepository.findById(id).get();
			original.update(body);
			verifyDependenciesExist(original);
			usageRepository.save(original);
			return new ResponseEntity<>(assembler.toResource(original), HttpStatus.OK);
		}
	}

	@Override
	public void verifyDependenciesExist(Usage entity) throws RestException {
		//user, machine, framework and engine depend on me.
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
		try{
			dependency = Engine.class.getName();
			engineRepository.findById(entity.getEngineId()).get();
		}catch(NoSuchElementException e){
			throw new RestException("Cannot find "+dependency+" by ID '"+entity.getEngineId()+"', so the "+me+" cannot be updated to refer to that "+dependency+".");
		}
	}

	@Override
	public void verifyDependentsNotExist(Usage entity) throws RestException {
		//Nothing depends on Usage
	}

}
