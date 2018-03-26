package org.safs.data.resource;

import org.safs.data.model.Machine;
import org.springframework.stereotype.Component;

/**
 * This class aims to convert the domain data to resource data for presentation layer.
 * @author Lei Wang
 *
 * @param <DomainType>
 * @param <ResourceType>
 */
@Component
public class MachineResourceAssembler extends ResourceAssembler<Machine, MachineResource>{

	@Override
	public MachineResource toResource(Machine o) {
		MachineResource resource = new MachineResource(o);
		return resource;
	}
}
