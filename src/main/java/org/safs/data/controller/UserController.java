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
import org.safs.data.model.History;
import org.safs.data.model.User;
import org.safs.data.repository.HistoryRepository;
import org.safs.data.repository.UserRepository;
import org.safs.data.resource.UserResource;
import org.safs.data.resource.UserResourceAssembler;
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
@ExposesResourceFor(User.class)
@RequestMapping(value=User.REST_BASE_PATH, produces=MediaType.APPLICATION_JSON_VALUE)
public class UserController implements Verifier<User>{
	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private UserResourceAssembler assembler;

	//================================== REST APIs controller ============================================
	@GetMapping
	public ResponseEntity<Collection<UserResource>> findAll(){
		Iterable<User> elements = userRepository.findAll();
		Collection<UserResource> resources = assembler.toResourceCollection(elements);
		return new ResponseEntity<>(resources, HttpStatus.OK);
	}

	@PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserResource> put(@RequestBody User body){
		try{
			Optional<User> element = userRepository.findById(body.getId());
			return new ResponseEntity<>(assembler.toResource(element.get()), HttpStatus.FOUND);
		}catch(NoSuchElementException nse){
			verifyDependenciesExist(body);
			User o = userRepository.save(body);
			log.debug(User.class.getName()+" has been created in the repository.");
			return new ResponseEntity<>(assembler.toResource(o), HttpStatus.CREATED);
		}
	}

	@GetMapping(value="{id}")
	public ResponseEntity<UserResource> find(@PathVariable String id){
		try{
			Optional<User> element = userRepository.findById(id);
			return new ResponseEntity<>(assembler.toResource(element.get()), HttpStatus.OK);
		}catch(NoSuchElementException nse){
			throw new RestException("Failed to find "+User.class.getName()+" by id '"+id+"'", HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value="{id}")
	public ResponseEntity<UserResource> delete(@PathVariable String id){
		try{
			verifyDependentsNotExist(userRepository.findById(id).get());
			userRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}catch(NoSuchElementException e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@PutMapping(value="{id}")
	public ResponseEntity<UserResource> update(@PathVariable String id, @RequestBody User body){
		if(!userRepository.existsById(id)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}else{
			User original = userRepository.findById(id).get();
			original.update(body);
			verifyDependenciesExist(original);
			userRepository.save(original);
			return new ResponseEntity<>(assembler.toResource(original), HttpStatus.OK);
		}
	}

	@Override
	public void verifyDependenciesExist(User entity) throws RestException {
		//depends on nothing
	}

	@Override
	public void verifyDependentsNotExist(User entity) throws RestException {
		String me = entity.getClass().getName();
		//'History' depends on me.
		String dependent = History.class.getName();
		if(!historyRepository.findAllByUserId(entity.getId()).isEmpty()){
			throw new RestException("Cannot delete "+me+" by id '"+entity.getId()+"', there are still "+dependent+"s depending on it!", HttpStatus.FAILED_DEPENDENCY);
		}
	}

}
