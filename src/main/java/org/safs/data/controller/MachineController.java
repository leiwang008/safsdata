/**
 * Copyright (C) SAS Institute, All rights reserved.
 * General Public License: http://www.opensource.org/licenses/gpl-license.php
 */

/**
 * Logs for developers, not published to API DOC.
 *
 * History:
 * @date 2018-03-22    (Lei Wang) Initial release.
 * @date 2018-03-30    (Lei Wang) Used constant to specify the path for annotation @RequestMapping
 *                                Removed the leading "/" for the path in each request-method, it is relative to the path of this class annotated with @RequestMapping(value=xxx).
 */
package org.safs.data.controller;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.safs.data.exception.RestException;
import org.safs.data.model.History;
import org.safs.data.model.Machine;
import org.safs.data.repository.HistoryRepository;
import org.safs.data.repository.MachineRepository;
import org.safs.data.resource.MachineResource;
import org.safs.data.resource.MachineResourceAssembler;
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
@ExposesResourceFor(Machine.class)
@RequestMapping(value=Machine.REST_BASE_PATH, produces=MediaType.APPLICATION_JSON_VALUE)
public class MachineController implements Verifier<Machine>{
	private static final Logger log = LoggerFactory.getLogger(MachineController.class);

	@Autowired
	private MachineRepository machineRepository;
	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private MachineResourceAssembler assembler;

	//================================== REST APIs controller ============================================
	@GetMapping
	public ResponseEntity<Collection<MachineResource>> findAll(){
		Iterable<Machine> elements = machineRepository.findAll();
		Collection<MachineResource> resources = assembler.toResourceCollection(elements);
		return new ResponseEntity<>(resources, HttpStatus.OK);
	}

	@PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MachineResource> create(@RequestBody Machine body){
		//TODO
		//org.springframework.dao.DataIntegrityViolationException: could not execute statement; SQL [n/a]; constraint [UKl2w0326a9aakpop55m113b4u8];
		//nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement
		//We may try to catch the ConstraintViolationException instead of checking the pair (name, ip) ourselves.
		//But if the machine exist, we need to get its data from repository and return it.

		//Since the machine's (name, ip) pair is unique, we will check if the machine exists or not before saving it.
		List<Machine> machines = machineRepository.findByNameAndIp(body.getName(), body.getIp());
		if(machines!=null && !machines.isEmpty()){
			log.debug(Machine.class.getName()+" has alredy existed in the repository.");
			return new ResponseEntity<>(assembler.toResource(machines.get(0)), HttpStatus.FOUND);
		}else{
			verifyDependenciesExist(body);
			Machine o = machineRepository.save(body);
			log.debug(Machine.class.getName()+" has been created in the repository.");
			return new ResponseEntity<>(assembler.toResource(o), HttpStatus.CREATED);
		}
	}

	@GetMapping(value="{id}")
	public ResponseEntity<MachineResource> find(@PathVariable Long id){
		try{
			Optional<Machine> element = machineRepository.findById(id);
			return new ResponseEntity<>(assembler.toResource(element.get()), HttpStatus.OK);
		}catch(NoSuchElementException nse){
			throw new RestException("Failed to find "+Machine.class.getName()+" by id '"+id+"'", HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value="{id}")
	public ResponseEntity<MachineResource> delete(@PathVariable Long id){
		try{
			verifyDependentsNotExist(machineRepository.findById(id).get());
			machineRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}catch(NoSuchElementException e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@PutMapping(value="{id}")
	public ResponseEntity<MachineResource> update(@PathVariable Long id, @RequestBody Machine body){
		if(!machineRepository.existsById(id)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}else{
			Machine original = machineRepository.findById(id).get();
			original.update(body);
			verifyDependenciesExist(original);
			machineRepository.save(original);
			return new ResponseEntity<>(assembler.toResource(original), HttpStatus.OK);
		}
	}

	@Override
	public void verifyDependenciesExist(Machine entity) throws RestException {
		//depends on nothing
	}

	@Override
	public void verifyDependentsNotExist(Machine entity) throws RestException {
		String me = entity.getClass().getName();
		//'History' depends on me.
		String dependent = History.class.getName();
		if(!historyRepository.findAllByMachineId(entity.getId()).isEmpty()){
			throw new RestException("Cannot delete "+me+" by id '"+entity.getId()+"', there are still "+dependent+"s depending on it!", HttpStatus.FAILED_DEPENDENCY);
		}
	}

}
